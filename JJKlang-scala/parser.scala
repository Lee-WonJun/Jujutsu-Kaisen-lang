package parser

import parsley.Parsley
import parsley.Parsley.*
import parsley.character.*
import parsley.combinator.{option}
import parsley.syntax.character.*
import java.util.concurrent.TimeUnit.*

import ast.*

val number = digit.foldLeft1[Int](0)((n, d) => n * 10 + d.asDigit)

val literal = (number <* spaces).map(Val.Num(_))

val variable =
  (letter <~> many(letterOrDigit | '_') <* spaces)
    .map((first, rest) => (first +: rest).mkString)

val value = literal | variable.map(Val.Var(_))

inline def sepBySpace[A](xs: Parsley[A]*): Parsley[A] =
  xs.reduceLeft(_ <* spaces *> _)

extension (x: String)
  inline def toWords = sepBySpace(x.split(" ").map(string)*).void

/** 이건 {변수명} 의 승리야 */
val negative =
  ("이건" *> spaces *> variable <* "의 승리야".toWords).map(Stmt.Neg(_))

/** 작별이다 {값 | 변수명 } 내가 없는 시대에 태어났을 뿐인 범부여 */
val `return` =
  ("작별이다" *> spaces *> value <~> option("내가 없는 시대에 태어났을 뿐인 범부여".toWords))
    .map((v, half) => Stmt.Return(v, half.isDefined))

/** 하! 마지막에는 {값 | 변수명} 을 내뱉어야지 */
val print =
  ("하!" *> spaces *> value <* "을 내뱉어야지".toWords).map(Stmt.Print(_))

/** 2{초 | 분 | 시 | 일 | 주 } 휴재애애앳!!! */
val sleep =
  val timeUnit =
    '초'.as(SECONDS.toSeconds)
      | '분'.as(MINUTES.toSeconds)
      | '시'.as(HOURS.toSeconds)
      | '일'.as(DAYS.toSeconds)
      | '주'.as(n => DAYS.toSeconds(n * 7))
  (number <* spaces <~> timeUnit <* spaces <* "휴재애애앳!!!")
    .map((n, u) => Stmt.Sleep(u(n)))
