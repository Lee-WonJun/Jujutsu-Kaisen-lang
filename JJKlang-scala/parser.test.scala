package parser

import munit.FunSuite
import parsley.Success
import ast.*

class ParserTests extends munit.FunSuite:
  test("variable parser should parse valid identifiers"):
    assertEquals(variable.parse("x"), Success("x"))
    assertEquals(variable.parse("abc"), Success("abc"))
    assertEquals(variable.parse("foo123"), Success("foo123"))
    assertEquals(variable.parse("camelCase"), Success("camelCase"))
    assertEquals(variable.parse("snake_case"), Success("snake_case"))
    assertEquals(variable.parse("x "), Success("x"))

  test("sleep parser should parse time units"):
    assertEquals(sleep.parse("2초 휴재애애앳!!!"), Success(Stmt.Sleep(2)))
    assertEquals(sleep.parse("1분 휴재애애앳!!!"), Success(Stmt.Sleep(60)))
    assertEquals(sleep.parse("1시 휴재애애앳!!!"), Success(Stmt.Sleep(3600)))
    assertEquals(sleep.parse("1일 휴재애애앳!!!"), Success(Stmt.Sleep(86400)))
    assertEquals(sleep.parse("1주 휴재애애앳!!!"), Success(Stmt.Sleep(604800)))

  test("negative parser"):
    assertEquals(negative.parse("이건 x 의 승리야"), Success(Stmt.Neg("x")))
    assertEquals(negative.parse("이건 foo123 의 승리야"), Success(Stmt.Neg("foo123")))
    assertEquals(
      negative.parse("이건 camelCase 의 승리야"),
      Success(Stmt.Neg("camelCase"))
    )
    assertEquals(
      negative.parse("이건 snake_case 의 승리야"),
      Success(Stmt.Neg("snake_case"))
    )
    assertEquals(negative.parse("이건 x 의 승리야"), Success(Stmt.Neg("x")))

  test("return parser"):
    assertEquals(`return`.parse("작별이다 42"), Success(Stmt.Return(Val.Num(42), false)))
    assertEquals(`return`.parse("작별이다 x"), Success(Stmt.Return(Val.Var("x"), false)))
    assertEquals(
        `return`.parse("작별이다 42 내가 없는 시대에 태어났을 뿐인 범부여"),
        Success(Stmt.Return(Val.Num(42), true))
    )
    assertEquals(
        `return`.parse("작별이다 x 내가 없는 시대에 태어났을 뿐인 범부여"),
        Success(Stmt.Return(Val.Var("x"), true))
    )
