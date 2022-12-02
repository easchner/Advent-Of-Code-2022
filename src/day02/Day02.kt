package day02

import readInputString

fun main() {
    fun part1(input: List<String>): Long {
        var totalScore = 0L

        // A(ZXY) B(XYZ) C(YZX)
        //   036    036    036
        val ptMap = mapOf(
            "A" to mapOf("Z" to 0L, "X" to 3L, "Y" to 6L),
            "B" to mapOf("X" to 0L, "Y" to 3L, "Z" to 6L),
            "C" to mapOf("Y" to 0L, "Z" to 3L, "X" to 6L)
        )

        for (line in input) {
            val hands = line.split(" ")

            totalScore += when (hands[1]) {
                "X" -> 1
                "Y" -> 2
                "Z" -> 3
                else -> 0
            }

            totalScore += ptMap.getOrDefault(hands[0], mapOf())
                .getOrDefault(hands[1], 0L)
        }

        return totalScore
    }

    fun part2(input: List<String>): Long {
        var totalScore = 0L

        // A(YZX) B(XYZ) C(ZXY)
        //   123    123    123
        val ptMap = mapOf(
            "A" to mapOf("Y" to 1L, "Z" to 2L, "X" to 3L),
            "B" to mapOf("X" to 1L, "Y" to 2L, "Z" to 3L),
            "C" to mapOf("Z" to 1L, "X" to 2L, "Y" to 3L)
        )

        for (line in input) {
            val hands = line.split(" ")

            totalScore += when (hands[1]) {
                "Y" -> 3
                "Z" -> 6
                else -> 0
            }

            totalScore += ptMap.getOrDefault(hands[0], mapOf())
                .getOrDefault(hands[1], 0L)
        }

        return totalScore
    }

    val testInput = readInputString("day02/test")
    val input = readInputString("day02/input")

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 15L)
    println(part1(input))

    check(part2(testInput) == 12L)
    println(part2(input))
}
