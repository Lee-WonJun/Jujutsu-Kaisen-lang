import scala.io.Source.*

import mainargs.*

import parser.*
import interpreter.*
import utils.*

object Main:
  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args.toSeq)

  @main def run(
      @arg(short = 'S', doc = "Sleep 함수를 실행하는 대신 콘솔에 해당하는 시간을 출력합니다.")
      noSleep: Flag = Flag(false),
      @arg(short = 'e', doc = "주어진 인자를 파일 경로가 아닌 코드로 해석합니다.")
      eval: Flag = Flag(false),
      @arg(short = 'r', doc = "값을 반환하는 대신 출력합니다.")
      printReturn: Flag = Flag(false),
      @arg(positional = true)
      target: String
  ) =
    given ctx: InterpreterContext =
      if noSleep.value then SleeplessContext else IoContext

    val code = if eval.value then target else fromFile(target).mkString
    def onExit(code: Int) =
      if printReturn.value then println(code) else sys.exit(code)

    program
      .parse(code)
      .map(Interpreter().eval(_).`return`)
      .fold(err => { println(err); 1 }, identity)
      |> onExit
