module AST

type Operator = Add | Subtract | Multiply | Divide

type LiteralInt = int
type VariableString = string
type Value =
    | Literal of LiteralInt
    | Variable of VariableString

type Statement =
    | Print of Value
    | Neg of VariableString
    | Sleep of int
    | Set of name:VariableString * value:Value
    | Calc of name:VariableString * value:Value * op:Operator list
    | If of Condition * el:Condition option
    | While of condition:Value * body:Block
    | Return of Value * division:bool
and Block = Statement list
and Condition = Value * Block