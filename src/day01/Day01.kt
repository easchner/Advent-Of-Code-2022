package day01

import readInputString

fun main() {
    fun getElves(input: List<String>): List<Long> {
        val elves = mutableListOf<Long>(0)
        for (line in input) {
            if (line == "") {
                elves.add(0)
            } else {
                elves[elves.lastIndex] += line.toLong()
            }
        }

        return elves
    }

    fun part1(input: List<String>): Long {
        return getElves(input).max()
    }

    fun part2(input: List<String>): Long {
        return getElves(input).sortedBy { it }.takeLast(3).sum()
    }

    val testInput = readInputString("day01/test")
    val input = readInputString("day01/input")

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 100L)
    println(part1(input))

    check(part2(testInput) == 100L)
    println(part2(input))
}
