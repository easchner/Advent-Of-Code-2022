package day06

import readInputString

fun main() {
    fun findFirstUniqueSet(input: String, size: Int): Int {
        for (i in input.indices) {
            val unique = input.substring(i, i + size).map { it }.toSet()
            if (unique.size == size) {
                return i + size
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        return findFirstUniqueSet(input[0], 4)
    }

    fun part2(input: List<String>): Int {
        return findFirstUniqueSet(input[0], 14)
    }

    val testInput = readInputString("day06/test")
    val input = readInputString("day06/input")

    check(part1(testInput) == 7)
    println(part1(input))

    check(part2(testInput) == 19)
    println(part2(input))
}