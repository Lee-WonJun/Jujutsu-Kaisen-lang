package parser

import parsley.Parsley
import parsley.Parsley.*
import parsley.character.*
import parsley.combinator.{option}
import parsley.syntax.character.*
import java.util.concurrent.TimeUnit.*

import ast.*

inline def sepBySpace[A](xs: Parsley[A]*): Parsley[A] =
  xs.reduceLeft(_ <* whitespaces *> _)
extension [A](x: Parsley[A]) inline def p: Parsley[A] = x <* whitespaces

extension (x: String)
  inline def toWords = sepBySpace(x.split(" ").map(string)*).void

val number = digit.foldLeft1[Int](0)((n, d) => n * 10 + d.asDigit)

val literal = (number <* whitespaces).map(Val.Num(_))

val variable =
  (letter <::> many(letterOrDigit | '_') <* whitespaces).map(_.mkString)

val value = literal | variable.map(Val.Var(_))

/** 네놈은 {변수A} 마저 {정수값 | 변수B} 이란 말이냐{연속된 연산자} */
val assign =
  val op =
    '.'.as(Op.Add) | '~'.as(Op.Sub) | '!'.as(Op.Mul) | '?'.as(Op.Div)
  ("네놈은".p *> variable <* whitespaces <* "마저".p <~> value <* whitespaces <* "이란 말이냐".toWords
    <~> many(op)).map { case ((a, b), ops) => Stmt.Assign(a, b, ops) }

/** 이건 {변수A} 의 승리야 */
val negative =
  ("이건" *> whitespaces *> variable <* "의 승리야".toWords).map(Stmt.Neg(_))

/** 작별이다 {값 | 변수명 } 내가 없는 시대에 태어났을 뿐인 범부여 */
val `return` =
  ("작별이다" *> whitespaces *> value <~> option("내가 없는 시대에 태어났을 뿐인 범부여".toWords))
    .map((v, half) => Stmt.Return(v, half.isDefined))

/** 하! 마지막에는 {정수값 | 변수A} 을 내뱉어야지 */
val print =
  ("하! 마지막에는".toWords *> whitespaces *> value <* "을 내뱉어야지".toWords)
    .map(Stmt.Print(_))

/** 2{초 | 분 | 시 | 일 | 주 } 휴재애애앳!!! */
val sleep =
  val timeUnit =
    '초'.as(SECONDS.toSeconds)
      | '분'.as(MINUTES.toSeconds)
      | '시'.as(HOURS.toSeconds)
      | '일'.as(DAYS.toSeconds)
      | '주'.as(n => DAYS.toSeconds(n * 7))
  (number <* whitespaces <~> timeUnit <* whitespaces <* "휴재애애앳!!!")
    .map((n, u) => Stmt.Sleep(u(n)))

val block: Parsley[List[Stmt]] = many(~statement) <* whitespaces

extension (p: Parsley[(Val, List[Stmt])])
  inline def toCond = p.map[Stmt.Condition](Stmt.Condition(_, _))

/** 넌 {정수값 | 변수A} 여서 {block} 인건가 아니면 {정수값 | 변수A} 여서 {block} 인건가 */
val condition: Parsley[Stmt] =
  val condition =
    ("넌".p *> value.p <* "여서".p <~> block.p <* "인건가".p).toCond
  val `else` =
    ("아니면".p *> value.p <* "이라서".p <~> block.p <* "인건가".p).toCond

  (condition <~> option(`else`)).map(Stmt.If(_, _))

/** 게속해서 가르쳐 주겠어 {조건}을! {block} 훗 에? 훗 */
val `while`: Parsley[Stmt] =
  ("게속해서 가르쳐 주겠어".toWords.p *> value.p <* "을".p <* "!".p <* whitespaces <~> block <* "훗 에? 훗".toWords).toCond
    .map(Stmt.While(_))

val statement =
  assign.p | `return`.p | print.p | sleep.p | condition.p | `while`.p | negative.p
