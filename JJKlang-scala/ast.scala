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
  case Assign(name: Identifier, value: Val, op: Iterable[Op])
  case If(`if`: Condition, `else`: Option[Condition])
  case While(`while`: Condition)
  case Return(v: Val, half: Boolean)

case class Condition(v: Val, b: Block)

type Block = Iterable[Stmt]
