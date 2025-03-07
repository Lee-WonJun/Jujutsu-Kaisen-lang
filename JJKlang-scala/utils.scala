package utils

extension [A](xs: IterableOnce[A])
  /** 리스트 내의 연속된 동일한 원소들을 세고, 각 원소와 그 원소가 연속으로 나타난 횟수를 쌍으로 표현한 리스트를 반환합니다.
    *
    * @tparam A
    *   리스트 원소의 타입
    * @return
    *   각 원소와 해당 원소의 연속 출현 횟수를 포함하는 쌍의 리스트
    *
    * ### 예시
    *
    * ```scala
    * List(1, 1, 2, 2, 2, 3).countConsecutive //=> List((1, 2), (2, 3), (3, 1))
    * ```
    */
  inline def countConsecutive: List[(A, Int)] =
    xs.iterator.toVector.foldRight(List.empty[(A, Int)]) { (x, acc) =>
      acc match
        case (y, n) :: rest if y == x => (x, n + 1) :: rest
        case _                        => (x, 1) :: acc
    }
