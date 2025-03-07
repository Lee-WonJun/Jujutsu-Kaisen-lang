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
