module Parser

open FParsec
open AST

let statementParser, statementParserRef = createParserForwardedToRef<Statement, Unit>()

let variableParser = 
    many1Satisfy2L isLetter (fun c -> isLetter c || isDigit c) "변수명" .>> spaces |>> VariableString

let literalParser = 
    pint32 .>> spaces |>> Literal

let pword s = pstring s .>> spaces

let valueParser = choice [literalParser; variableParser |>> Variable]

let assignmentOrCalcParser = 
    // 네놈은 {변수명} 마저 {값 | 변수명 }이란 말이냐 {연산자}
    let specialCharParser = satisfy (fun c -> c = '.' || c = '~' || c = '!' || c = '?')
    let specialCharsSequenceParser = many specialCharParser
    pword "네놈은" >>. spaces >>.
    variableParser .>> pstring "마저" .>> spaces .>>. valueParser  .>> 
    spaces .>> skipString "이란 말이냐" .>>. specialCharsSequenceParser 
    |>> fun ((variable, value), p) -> 
        let op = p |> List.choose (function | '.' -> Some Add | '~' -> Some Subtract | '!' -> Some Multiply | '?' -> Some Divide | _ -> None)
        match op with
        | [] -> Set (variable, value)
        | _ -> Calc (variable, value, op)

let negetiveParser = 
    // 이건 {값 | 변수명} 의 승리야
    pword "이건" >>. spaces >>. variableParser .>> spaces .>> pword "의 승리야"
    |>> fun x -> Neg (x)

let returnParser =
    //  작별이다 {값 | 변수명 } 내가 없는 시대에 태어났을 뿐인 범부여
    skipString "작별이다" >>. spaces >>. valueParser .>> spaces .>>. opt (pword "내가 없는 시대에 태어났을 뿐인 범부여")
    |>> function 
        | (value, None) -> Return (value, false)
        | (value, Some _) -> Return (value ,true)

let printParser = 
    //  하! 마지막에는 {값 | 변수명} 을 내뱉어야지
    pword "하! 마지막에는" >>. spaces >>. valueParser .>> spaces .>> pword "을 내뱉어야지"
    |>> Print 

let sleepParser = 
    // 2{초 | 분 | 시 | 일 | 주 } 휴재애애앳!!!
    let timeUnitParser = choice [pstring "초"; pstring "분"; pstring "시"; pstring "일"; pstring "주"]
    pint32 .>> spaces .>>. timeUnitParser .>> spaces .>> skipString "휴재애애앳!!!"
    |>> fun (time, timeUnit) ->     
        let sleepTime = 
            match timeUnit with
            | "초" -> time
            | "분" -> time * 60
            | "시" -> time * 60 * 60
            | "일" -> time * 60 * 60 * 24
            | "주" -> time * 60 * 60 * 24 * 7
            | _ -> 0
        Sleep sleepTime
        

let conditionParser = 
    let endblockParser = 
        (many statementParser) .>> spaces .>> pword "인건가"


    // 넌 {value} 여서 {block} 인건가 아니면 {value} 여서 {block} 인건가 
    let conditionStartParser = pword "넌" >>. spaces >>. valueParser .>> spaces .>> pword "여서" .>> spaces
    let elseStartParser = opt(pword "아니면" >>. spaces >>. valueParser .>> spaces .>> pword "이라서" .>> spaces .>>. endblockParser)

    pipe3 conditionStartParser endblockParser elseStartParser (fun x y z->
        match z with
        | Some (z1,z2) -> If ((x, y), Some (Condition(z1,z2)))
        | None -> If ((x, y), None))

let whileParser = 
    let endblockParser = 
        (many statementParser) .>> spaces .>> pword "훗 에? 훗"
    // 게속해서 가르쳐 주겠어 {조건}을! {block} 훗 에? 훗
    pword "게속해서 가르쳐 주겠어" >>. spaces >>. valueParser .>> spaces .>> pword "을!" .>> spaces .>>. endblockParser
    |>> fun (x, y) -> While (x, y)

do statementParserRef.Value <- choice [
    assignmentOrCalcParser .>> spaces;
    returnParser.>> spaces ;
    printParser .>> spaces;
    sleepParser .>> spaces ;
    conditionParser .>> spaces;
    whileParser .>> spaces;
    negetiveParser .>> spaces;
    ]