package day09

import readInputString

data class Position(var x: Int, var y: Int)

fun main() {
    fun moveHead(head: Position, direction: String) {
        when (direction) {
            "U" -> head.y++
            "D" -> head.y--
            "L" -> head.x--
            "R" -> head.x++
        }
    }

    fun moveKnot(head: Position, tail: Position): Boolean {
        if (tail.y < head.y - 1) {
            when {
                tail.x < head.x - 1 -> tail.x++
                tail.x > head.x + 1 -> tail.x--
                else -> tail.x = head.x
            }
            tail.y = head.y - 1
            return true
        }
        if (tail.y > head.y + 1) {
            when {
                tail.x < head.x - 1 -> tail.x++
                tail.x > head.x + 1 -> tail.x--
                else -> tail.x = head.x
            }
            tail.y = head.y + 1
            return true
        }
        if (tail.x > head.x + 1) {
            tail.x = head.x + 1
            when {
                tail.y < head.y - 1 -> tail.y++
                tail.y > head.y + 1 -> tail.y--
                else -> tail.y = head.y
            }
            return true
        }
        if (tail.x < head.x - 1) {
            tail.x = head.x - 1
            when {
                tail.y < head.y - 1 -> tail.y++
                tail.y > head.y + 1 -> tail.y--
                else -> tail.y = head.y
            }
            return true
        }

        return false
    }

    fun part1(input: List<String>): Int {
        val headPosition = Position(0, 0)
        val tailPosition = Position(0, 0)
        val positions = mutableSetOf<Position>()

        for (line in input) {
            val inputs = line.split(" ")
            val direction = inputs[0]
            val distance = inputs[1].toInt()

            for (i in 0 until distance) {
                moveHead(headPosition, direction)
                moveKnot(headPosition, tailPosition)
                positions.add(Position(tailPosition.x, tailPosition.y))
            }
        }

        return positions.size
    }

    fun part2(input: List<String>): Int {
        val knots = Array(10) { Position(0, 0) }
        val positions = mutableSetOf<Position>()

        for (line in input) {
            val inputs = line.split(" ")
            val direction = inputs[0]
            val distance = inputs[1].toInt()

            for (i in 0 until distance) {
                moveHead(knots[0], direction)
                for (k in 0 until knots.size - 1) {
                    val moved = moveKnot(knots[k], knots[k + 1])
                    if (!moved)
                        break
                }
                positions.add(Position(knots.last().x, knots.last().y))
            }
        }

        return positions.size
    }

    val testInput = readInputString("day09/test")
    val testInput2 = readInputString("day09/test2")
    val input = readInputString("day09/input")

    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput2) == 36)
    println(part2(input))
}