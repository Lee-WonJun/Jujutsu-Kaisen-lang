package ast

enum Op:
  case Add, Sub, Mul, Div

type Identifier = String

enum Val:
  case Num(v: Int)
  case Var(v: Identifier)

enum Stmt:
  case Print(name: Val)
  case Neg(name: Identifier)
  case Sleep(seconds: Long)
  case Set(name: Identifier, value: Val)
  case If(`if`: Condition, `else`: Option[Condition])
  case While(`while`: Condition, body: Block)
  case Return(v: Val, half: Boolean)
  case Condition(v: Val, b: Block)

type Block = Iterable[Stmt]
