# 주술회전 프로그래밍 언어 (스칼라 구현체)

고죠 사토루 네놈은 JVM 마저 최강인 것이냐!!

## 실행

`scala`에 `project.scala` 가 있는 폴더를 기준으로 실행하면 됩니다.

```sh
# 실행
scala JJKlang-scala -- JJKlang/sample.jjk
# 대기 시간 없이 실행
scala JJKlang-scala -- JJKlang/sample.jjk --no-sleep
scala --watch JJKlang-scala 

# 테스트
scala test JJKlang-scala
scala test  --watch JJKlang-scala
```

## 개발

[metals](https://scalameta.org/metals/) + [scala-cli](https://scala-cli.virtuslab.org)를 사용합니다.

```sh
# Metals LSP 환경 구축
cd JJKlang-scala
scala setup-ide .
```

## 패키징

[실행 파일을 패키징](https://scala-cli.virtuslab.org/docs/commands/package)하여 더 빠르게 실행할 수 있습니다.

```sh
scala-cli config power true

# JVM
scala --power package JJKlang-scala -o jjk-scala.jar -f

# GraalVM
scala --power package JJKlang-scala -o jjk-scala.graal --native-image -f
./jjk-scala.graal JJKlang/sample.jjk

# Scala Native
scala --power package JJKlang-scala -o jjk-scala.native --native -f
```

### 벤치마크

```sh
hyperfine --warmup 5 --export-markdown benchmark.md -N \
  './jjk-scala.native -- JJKlang/sample.jjk --no-sleep' \
  './jjk-scala.graal -- JJKlang/sample.jjk --no-sleep' \
  './jjk-scala.jvm -- JJKlang/sample.jjk --no-sleep' \
  'scala JJKlang-scala -- JJKlang/sample.jjk --no-sleep'
```

| Command | Mean [ms] | Min [ms] | Max [ms] | Relative |
|:---|---:|---:|---:|---:|
| `./jjk-scala.native -- JJKlang/sample.jjk --no-sleep` | 3.3 ± 0.9 | 2.0 | 8.5 | 1.00 |
| `./jjk-scala.graal -- JJKlang/sample.jjk --no-sleep` | 7.4 ± 1.0 | 5.5 | 11.0 | 2.23 ± 0.66 |
| `./jjk-scala.jvm -- JJKlang/sample.jjk --no-sleep` | 556.3 ± 21.3 | 521.3 | 581.1 | 167.44 ± 44.02 |
| `scala JJKlang-scala -- JJKlang/sample.jjk --no-sleep` | 758.3 ± 12.9 | 734.8 | 780.5 | 228.25 ± 59.49 |
