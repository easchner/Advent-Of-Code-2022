package day11

import readInputSpaceDelimited

class Monkey {
    var items = mutableListOf<Long>()
    var trueMonkey = 0
    var falseMonkey = 0
    var isMultiply = false
    var opValue = 0L
    var opOld = false
    var testValue = 0L
    var numThrows = 0L

    fun operation (item: Long): Long {
        val opVal = if (opOld) item else opValue
        return if (isMultiply) {
            item * opVal
        } else {
            item + opVal
        }
    }

    fun throwItem (item: Long): Int {
        return if (item % testValue == 0L) {
            trueMonkey
        } else {
            falseMonkey
        }
    }
}

fun main() {
    fun parseInput(input: List<List<String>>): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()

        for (monkey in input) {
            val newMonkey = Monkey()

            // items
            val items = monkey[1].replace(",", "").split(" ")
            for (i in 4 until items.size) {
                newMonkey.items.add(items[i].toLong())
            }

            // operation
            val ops = monkey[2].split(" ")
            if (ops[6] == "*") {
                newMonkey.isMultiply = true
            }
            if (ops[7] == "old") {
                newMonkey.opOld = true
            } else {
                newMonkey.opValue = ops.last().toLong()
            }

            // test
            newMonkey.testValue = monkey[3].split(" ").last().toLong()

            // trueMonkey / falseMonkey
            newMonkey.trueMonkey = monkey[4].split(" ").last().toInt()
            newMonkey.falseMonkey = monkey[5].split(" ").last().toInt()

            monkeys.add(newMonkey)
        }

        return monkeys
    }

    fun part1(input: List<List<String>>): Long {
        val monkeys = parseInput(input)

        for (turn in 0 until 20) {
            for (monkey in monkeys) {
                for (item in monkey.items) {
                    val worry = monkey.operation(item) / 3L
                    val whichMonkey = monkey.throwItem(worry)
                    monkeys[whichMonkey].items.add(worry)
                    monkey.numThrows++
                }
                monkey.items.clear()
            }
        }

        val topTwo = monkeys.sortedByDescending { it.numThrows }.take(2)
        return topTwo[0].numThrows * topTwo[1].numThrows
    }

    fun part2(input: List<List<String>>): Long {
        val monkeys = parseInput(input)
        var maxStress = 1L
        monkeys.map { it.testValue }.forEach { maxStress *= it }

        for (turn in 0 until 10_000) {
            for (monkey in monkeys) {
                for (item in monkey.items) {
                    val worry = monkey.operation(item) % maxStress
                    val whichMonkey = monkey.throwItem(worry)
                    monkeys[whichMonkey].items.add(worry)
                    monkey.numThrows++
                }
                monkey.items.clear()
            }
        }

        val topTwo = monkeys.sortedByDescending { it.numThrows }.take(2)
        return topTwo[0].numThrows * topTwo[1].numThrows
    }

    val testInput = readInputSpaceDelimited("day11/test")
    val input = readInputSpaceDelimited("day11/input")

    check(part1(testInput) == 10_605L)
    println(part1(input))

    check(part2(testInput) == 2_713_310_158L)
    println(part2(input))
}