package interpreter

import ast.*
import ast.Stmt.*
import utils.*
import scala.annotation.tailrec

trait InterpreterContext:
  def print(s: String): Unit
  def sleep(seconds: Long): Unit

object IoContext extends InterpreterContext:
  def print(s: String): Unit = println(s)
  def sleep(seconds: Long): Unit = Thread.sleep(seconds * 1000)

object SleeplessContext extends InterpreterContext:
  def print(s: String): Unit = println(s)
  def sleep(seconds: Long): Unit = println(s"${seconds}초 대기!!!")

case class LoggerContext(
    log: scala.collection.mutable.ListBuffer[String] =
      scala.collection.mutable.ListBuffer.empty[String]
) extends InterpreterContext:
  def print(s: String): Unit = log += s"[print] $s"
  def sleep(seconds: Long): Unit = log += s"[wait] $seconds"

case class Interpreter(
    vars: Map[String, Int] = Map.empty,
    `return`: Int = 0
)(using ctx: InterpreterContext = IoContext):
  extension (v: Val)
    inline def isTrue: Boolean = v.get != 0
    inline def get: Int = v match
      case Val.Num(n) => n
      case Val.Var(x) => vars.getOrElse(x, 0)

  def eval(stmts: Iterable[Stmt]): Interpreter =
    stmts.foldLeft(this)((acc, stmt) => acc.eval(stmt))

  @tailrec final def eval(stmt: Stmt): Interpreter = stmt match
    case Print(name)    => ctx.print(name.get.toString); this
    case Sleep(seconds) => ctx.sleep(seconds); this
    case Neg(name)      => this.copy(vars = vars.updated(name, -vars(name)))

    case Assign(name, value, Nil) =>
      this.copy(vars = vars.updated(name, value.get))

    case Assign(name, value, ops) =>
      val result = ops.countConsecutive.foldLeft(value.get) {
        case (acc, (op, n)) =>
          op match
            case Op.Add => acc + n
            case Op.Sub => acc - n
            case Op.Mul => acc * n
            case Op.Div => acc / n
      }
      this.copy(vars = vars.updated(name, result))

    case If(Condition(ifCond, ifBlock), elseBranch) =>
      if ifCond.isTrue then eval(ifBlock)
      else
        elseBranch match
          case Some(Condition(elseCond, elseBlock)) if elseCond.isTrue =>
            eval(elseBlock)
          case _ => this

    case `while` @ While(Condition(cond, block)) =>
      if cond.isTrue then eval(block).eval(`while`) else this

    case Return(value, div) =>
      this.copy(`return` = value.get / (if div then 2 else 1))
