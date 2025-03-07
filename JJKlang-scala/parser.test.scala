package parser

import munit.FunSuite
import parsley.Success
import ast.*

class ParserTests extends munit.FunSuite:
  test("자료형"):
    // assertEquals(number.parse("-3"), Success(-3))
    assertEquals(number.parse("0"), Success(0))
    assertEquals(number.parse("42"), Success(42))
    assertEquals(number.parse("123"), Success(123))
    assertEquals(number.parse("9999"), Success(9999))
    assertEquals(literal.parse("42 "), Success(Val.Num(42)))
    assertEquals(value.parse("42"), Success(Val.Num(42)))

  test("literal parser should parse numbers with trailing spaces"):
    assertEquals(literal.parse("123   "), Success(Val.Num(123)))
    assertEquals(literal.parse("0 "), Success(Val.Num(0)))
    assertEquals(literal.parse("42  "), Success(Val.Num(42)))

  test("자료_할당"):
    assertEquals(
      assign.parse("네놈은 x 마저 42 이란 말이냐"),
      Success(Stmt.Assign("x", Val.Num(42), Nil))
    )
    assertEquals(
      assign.parse("네놈은 최강 마저 5 이란 말이냐!!..!"),
      Success(
        Stmt.Assign(
          "최강",
          Val.Num(5),
          List(Op.Mul, Op.Mul, Op.Add, Op.Add, Op.Mul)
        )
      )
    )

  test("print parser should parse print statements"):
    assertEquals(
      print.parse("하! 마지막에는 42 을 내뱉어야지"),
      Success(Stmt.Print(Val.Num(42)))
    )
    assertEquals(
      print.parse("하! 마지막에는 x 을 내뱉어야지"),
      Success(Stmt.Print(Val.Var("x")))
    )

  test("variable"):
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

  test("단항_연산자"):
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
    assertEquals(
      `return`.parse("작별이다 42"),
      Success(Stmt.Return(Val.Num(42), false))
    )
    assertEquals(
      `return`.parse("작별이다 x"),
      Success(Stmt.Return(Val.Var("x"), false))
    )
    assertEquals(
      `return`.parse("작별이다 42 내가 없는 시대에 태어났을 뿐인 범부여"),
      Success(Stmt.Return(Val.Num(42), true))
    )
    assertEquals(
      `return`.parse("작별이다 x 내가 없는 시대에 태어났을 뿐인 범부여"),
      Success(Stmt.Return(Val.Var("x"), true))
    )

  test("조건문"):
    import ast.Stmt.*
    import ast.Val.*

    val parsed = statement.parse("""넌 고죠사토루 여서
  게속해서 가르쳐 주겠어 사랑 을!
      네놈은 사랑 마저 사랑 이란 말이냐~
      하! 마지막에는 사랑 을 내뱉어야지
  훗 에? 훗
인건가
아니면 고죠사토루 이라서
  하! 마지막에는 저주하는말 을 내뱉어야지
인건가""")
    assertEquals(
      parsed,
      Success(
        If(
          Condition(
            Var("고죠사토루"),
            List(
              While(
                Condition(
                  Var("사랑"),
                  List(
                    Assign("사랑", Var("사랑"), List(Op.Sub)),
                    Print(Var("사랑"))
                  )
                )
              )
            )
          ),
          Some(
            Condition(Var("고죠사토루"), List(Print(Var("저주하는말"))))
          )
        )
      )
    )
