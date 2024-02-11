module Interpreter
open AST
open Parser

open System
open System.Collections.Generic


type ExecutionContext = {
    Variables: Dictionary<VariableString, int32>
    mutable ReturnedValue: int32
}

let createExecutionContext () = {
    Variables = new Dictionary<VariableString, int32>()
    ReturnedValue = 0
}


let getValue ctx (name: VariableString) =
    ctx.Variables.[name]

// 변수 값을 설정하는 함수
let setValue ctx (name: VariableString) (value: Value) =
    match value with
    | Literal l -> ctx.Variables.[name] <- l
    | Variable v -> ctx.Variables.[name] <- getValue ctx v


let compressOps lst =
    lst
    |> List.fold (fun acc elem ->
        match acc with
        | (lastElem, n) :: tail when lastElem = elem -> (elem, n + 1) :: tail
        | _ -> (elem, 1) :: acc
    ) []
    |> List.rev

// Value를 평가하는 함수
let evaluateValue (ctx: ExecutionContext) (value: Value) : bool =
    match value with
    | Literal l -> l <> 0
    | Variable v -> getValue ctx v <> 0



// Statement를 실행하는 함수
let rec executeStatement (ctx: ExecutionContext) (statement: Statement) =
    match statement with
    | Print value ->
        match value with
        | Literal l -> printfn "%d" l
        | Variable v -> printfn "%d" (getValue ctx v)
    | Sleep time -> System.Threading.Thread.Sleep(time * 1000)
    | Neg value ->
        let v = getValue ctx value
        setValue ctx value (Literal (-v))
    | Set (name, value) -> setValue ctx name value
    | Calc (name, value, ops) ->
        let v = 
            match value with
            | Literal l -> l
            | Variable v -> getValue ctx v  

        let co = compressOps ops

        let result = 
            co
            |> List.fold (fun acc (op, n) ->
                match op with
                | Add -> acc + n
                | Subtract -> acc -  n
                | Multiply -> acc * n
                | Divide -> acc / n
            ) v
            |> Literal

        setValue ctx name result

    | If ((value,block), elseCondition) ->
        let evaluateValue = evaluateValue ctx
        let isTrue = evaluateValue value
        if isTrue then
            block |> List.iter (executeStatement ctx)
        else
            match elseCondition with
            | Some (value, block) -> 
                let isTrue = evaluateValue value
                if isTrue then
                    block |> List.iter (executeStatement ctx)
            | None -> ()
    | While (value, body) ->
        let evaluateValue = evaluateValue ctx
        while evaluateValue value do
            body |> List.iter (executeStatement ctx)
    | Return (value, division) ->
        let l1 : int32 = 
            match value with
            | Literal l -> l
            | Variable v -> getValue ctx v
        let l2 = if division then 2 else 1
        ctx.ReturnedValue <- l1 / l2

// Block을 실행하는 함수
let executeBlock (ctx: ExecutionContext) (block: Block) =
    block |> List.iter (executeStatement ctx)
