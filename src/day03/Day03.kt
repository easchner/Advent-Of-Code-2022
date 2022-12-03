package day03

import readInputString

fun main() {
    fun calculatePriority(c: Char): Long {
        var priority = 0L
        if (c.isUpperCase()) {
            priority += 26L
        }

        priority += c.lowercaseChar().code - 96
        return priority
    }

    fun part1(input: List<String>): Long {
        var priorityTotal = 0L

        for (line in input) {
            val sack1 = line.substring(0, line.length / 2)
            val sack2 = line.substring(line.length / 2, line.length)

            for (c in sack1) {
                if (sack2.contains(c)) {
                    priorityTotal += calculatePriority(c)
                    break
                }
            }
        }

        return priorityTotal
    }

    fun part2(input: List<String>): Long {
        var priorityTotal = 0L

        for (i in 0 until input.size / 3) {
            val sack1 = input[i * 3]
            val sack2 = input[i * 3 + 1]
            val sack3 = input[i * 3 + 2]

            for (c in sack1) {
                if (sack2.contains(c) && sack3.contains(c)) {
                    priorityTotal += calculatePriority(c)
                    break
                }
            }
        }

        return priorityTotal
    }

    val testInput = readInputString("day03/test")
    val input = readInputString("day03/input")

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 157L)
    println(part1(input))

    check(part2(testInput) == 70L)
    println(part2(input))
}
