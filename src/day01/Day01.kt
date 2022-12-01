package day01

import readAndCompoundSpaceDelimited
import readInputSpaceDelimited
import readInputString

fun main() {
    fun part1(input: List<Long>): Long {
        return input.max()
    }

    fun part2(input: List<Long>): Long {
        return input.sortedBy { it }.takeLast(3).sum()
    }

    val testInput = readAndCompoundSpaceDelimited("day01/test") { it.sumOf { it.toLong() } }
    val input = readAndCompoundSpaceDelimited("day01/input") { it.sumOf { it.toLong() }}

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 24_000L)
    println(part1(input))

    check(part2(testInput) == 45_000L)
    println(part2(input))
}
