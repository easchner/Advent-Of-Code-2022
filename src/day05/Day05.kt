package day05

import readInputSpaceDelimited
import java.util.Stack

fun main() {
    fun getStacks(input: List<String>): List<Stack<Char>> {
        val stacks = mutableListOf<Stack<Char>>()

        for (line in input) {
            val containers = line.chunked(4).map {
                it.substring(1, 2)[0]
            }

            for (i in containers.indices) {
                val item = containers[i]
                if (item in 'A'..'Z') {
                    while (stacks.size <= i) {
                        stacks.add(Stack())
                    }
                    stacks[i].push(item)
                }
            }
        }

        for (stack in stacks) {
            stack.reverse()
        }

        return stacks
    }

    fun part1(input: List<List<String>>): String {
        val stacks = getStacks(input[0])

        for (line in input[1]) {
            // Expected format: move 1 from 2 to 1
            val nums = line.split(" ")
            val amount = nums[1].toInt()
            val from = nums[3].toInt() - 1
            val target = nums[5].toInt() - 1

            for (i in 0 until amount) {
                stacks[target].push(stacks[from].pop())
            }
        }

        return stacks.map { if (it.isNotEmpty()) it.pop() else "" }.joinToString("")
    }

    fun part2(input: List<List<String>>): String {
        val stacks = getStacks(input[0])

        for (line in input[1]) {
            // move 1 from 2 to 1
            val nums = line.split(" ")
            val amount = nums[1].toInt()
            val from = nums[3].toInt() - 1
            val target = nums[5].toInt() - 1

            val temp = Stack<Char>()
            for (i in 0 until amount) {
                temp.push(stacks[from].pop())
            }
            while(temp.isNotEmpty()) {
                stacks[target].push(temp.pop())
            }
        }

        return stacks.map { if (it.isNotEmpty()) it.pop() else "" }.joinToString("")
    }

    val testInput = readInputSpaceDelimited("day05/test")
    val input = readInputSpaceDelimited("day05/input")

    check(part1(testInput) == "CMZ")
    println(part1(input))

    check(part2(testInput) == "MCD")
    println(part2(input))
}