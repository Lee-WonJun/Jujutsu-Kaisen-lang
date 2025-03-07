package interpreter

import scala.io.Source.*

import munit.FunSuite
import parser.program
import parsley.Success

class InterpreterTest extends FunSuite:
  val example = fromResource("sample.jjk").mkString

  test("예시_코드"):
    val logger = LoggerContext()
    given ctx: InterpreterContext = logger
    val interpreter = Interpreter()
    assertEquals(
      program.parse(example).map(interpreter.eval),
      Success(
        Interpreter(
          vars = Map("최강" -> -7, "사랑" -> 0, "저주하는말" -> 14, "고죠사토루" -> -14),
          `return` = -3
        )
      )
    )
    assertEquals(
      logger.log.toList,
      List(
        "[wait] 2",
        "[print] 14",
        "[print] 4",
        "[print] 3",
        "[print] 2",
        "[print] 1",
        "[print] 0"
      )
    )

  test("작별이다_최강"):
    val interpreter = Interpreter()(using SleeplessContext)
    assertEquals(
      program
        .parse("네놈은 최강 마저 5 이란 말이냐!!..! 작별이다 최강")
        .map(interpreter.eval)
        .map(_.`return`),
      Success(12)
    )
    assertEquals(
      program
        .parse("네놈은 최강 마저 5 이란 말이냐!!..! 작별이다 최강 내가 없는 시대에 태어났을 뿐인 범부여")
        .map(interpreter.eval)
        .map(_.`return`),
      Success(6)
    )
