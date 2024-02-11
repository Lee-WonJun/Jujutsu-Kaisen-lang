open FParsec
open Parser
open Interpreter

let ctx = createExecutionContext()

let runSourceFile fpath =
    match runParserOnFile (many statementParser) () fpath System.Text.Encoding.UTF8 with
    | Success (result, _, _) -> executeBlock ctx result
    | Failure (error, _, _) -> printfn "%s" error


[<EntryPoint>]
let main argv =
    match argv with
    | [|fpath|] -> runSourceFile fpath
    | _ -> printfn "Usage: JJKlang <source file>"
    ctx.ReturnedValue



