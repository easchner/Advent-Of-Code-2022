package day10

import readInputString

fun main() {
    fun part1(input: List<String>): Int {
        val cycles = Array(input.size * 2) { 1 }
        var currentCycle = 1
        var register = 1

        for (line in input) {
            if (line == "noop") {
                currentCycle++
                cycles[currentCycle] = register
            } else {
                val v = line.split(" ").last().toInt()
                currentCycle++
                cycles[currentCycle] = register
                currentCycle++
                register += v
                cycles[currentCycle] = register
            }
        }

        return listOf(20, 60, 100, 140, 180, 220).map { it * cycles[it] }.sum()
    }

    fun part2(input: List<String>): Int {
        val cycles = Array(input.size * 2) { 1 }
        var currentCycle = 0
        var register = 1

        for (line in input) {
            if (line == "noop") {
                currentCycle++
                cycles[currentCycle] = register
            } else {
                val v = line.split(" ").last().toInt()
                currentCycle++
                cycles[currentCycle] = register
                currentCycle++
                register += v
                cycles[currentCycle] = register
            }
        }

        var output = ""
        for (i in 0 until 6) {
            for (j in 0 until 40) {
                val spot = cycles[i*40 + j]
                output += if (spot in (j-1)..(j+1)) {
                    "#"
                } else {
                    " "
                }
            }
            output += "\n"
        }
        println(output)
        return 0
    }

    val testInput = readInputString("day10/test")
    val input = readInputString("day10/input")

    check(part1(testInput) == 13_140)
    println(part1(input))

    check(part2(testInput) == 0)
    println(part2(input))
}