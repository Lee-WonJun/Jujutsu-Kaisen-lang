package utils

import munit.FunSuite

class ConsecutiveSuite extends FunSuite:
  test("countConsecutive should return empty list for empty input"):
    assertEquals(List().countConsecutive, Nil)

  test("countConsecutive should count single elements as 1"):
    assertEquals(List(1, 2, 3).countConsecutive, List((1, 1), (2, 1), (3, 1)))

  test("countConsecutive should count repeated elements"):
    assertEquals(
      List(1, 1, 2, 2, 2, 3).countConsecutive,
      List((1, 2), (2, 3), (3, 1))
    )

  test("countConsecutive should work with strings"):
    assertEquals(
      List("a", "a", "b", "c", "c", "c").countConsecutive,
      List(("a", 2), ("b", 1), ("c", 3))
    )

  test("countConsecutive should handle alternating elements"):
    assertEquals(
      List(1, 2, 1, 2, 1, 2).countConsecutive,
      List((1, 1), (2, 1), (1, 1), (2, 1), (1, 1), (2, 1))
    )

  test("countConsecutive should work with a single element"):
    assertEquals(List(42).countConsecutive, List((42, 1)))
