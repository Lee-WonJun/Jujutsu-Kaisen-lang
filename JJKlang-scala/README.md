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

# Scala Native
scala --power package JJKlang-scala -o jjk-scala.native --native -f
```

### 벤치마크

```sh
hyperfine --warmup 5 --export-markdown benchmark.md -N \
  './jjk-scala.native -rS JJKlang/sample.jjk' \
  './jjk-scala.graal -rS JJKlang/sample.jjk' \
  './jjk-scala.jar -rS JJKlang/sample.jjk' \
  'scala JJKlang-scala -- -rS JJKlang/sample.jjk'
```

| Command | Mean [ms] | Min [ms] | Max [ms] | Relative |
|:---|---:|---:|---:|---:|
| `./jjk-scala.native -rS JJKlang/sample.jjk` | 2.2 ± 0.3 | 1.8 | 3.5 | 1.00 |
| `./jjk-scala.graal -rS JJKlang/sample.jjk` | 5.7 ± 0.8 | 4.9 | 8.8 | 2.60 ± 0.50 |
| `./jjk-scala.jar -rS JJKlang/sample.jjk` | 418.4 ± 21.9 | 395.0 | 466.3 | 191.46 ± 25.89 |
| `scala JJKlang-scala -- -rS JJKlang/sample.jjk` | 612.4 ± 13.2 | 595.7 | 640.4 | 280.19 ± 35.46 |
