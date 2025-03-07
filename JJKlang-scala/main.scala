import scala.io.Source.*

import parser.*
import interpreter.*

def run(path: String)(using ctx: InterpreterContext) = for
  file = fromFile(path).mkString
  interpreter = Interpreter()(using ctx)
  code <- program.parse(file)
yield interpreter.eval(code).`return`

@main def main(args: String*) = args.toList.partition(_.startsWith("--")) match
  case (flags, List(file)) =>
    given ctx: InterpreterContext =
      if flags.toSet("--no-sleep") then SleeplessContext else IoContext

    run(file)(using ctx).fold(err => { println(err); 1 }, identity)

  case _ => println("Usage: <program> -- <file> [--no-sleep]"); 1
