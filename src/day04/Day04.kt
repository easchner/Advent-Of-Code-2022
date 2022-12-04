package day04

import readInputString

fun main() {
    fun part1(input: List<String>): Long {
        var totalOverlap = 0L

        for (line in input) {
            val elves = line.split(",").map {
                it.split("-").map {
                    it.toInt()
                }
            }.map {
                (it[0]..it[1]).toSet()
            }

            if (elves[0].intersect(elves[1]) == elves[0] ||
                elves[1].intersect(elves[0]) == elves[1]) {
                totalOverlap++
            }
        }

        return totalOverlap
    }

    fun part2(input: List<String>): Long {
        var totalOverlap = 0L

        for (line in input) {
            val elves = line.split(",").map {
                it.split("-").map {
                    it.toInt()
                }
            }.map {
                (it[0]..it[1]).toSet()
            }

            if (elves[0].intersect(elves[1]).isNotEmpty()) {
                totalOverlap++
            }
        }

        return totalOverlap
    }

    val testInput = readInputString("day04/test")
    val input = readInputString("day04/input")

    check(part1(testInput) == 2L)
    println(part1(input))

    check(part2(testInput) == 4L)
    println(part2(input))
}
