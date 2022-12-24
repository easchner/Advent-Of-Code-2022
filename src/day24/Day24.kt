package day24

import readInputString
import java.lang.Math.abs
import java.util.PriorityQueue
import kotlin.system.measureNanoTime

enum class Direction { UP, DOWN, LEFT, RIGHT }

data class Blizzard(var x: Int, var y: Int, var dir: Direction)
data class Point(var x: Int, var y: Int)

fun main() {
    var maxX = 0
    var maxY = 0
    fun moveBlizzards(blizzards: List<Blizzard>) {
        for (blizzard in blizzards) {
            var nextX = blizzard.x
            var nextY = blizzard.y
            when (blizzard.dir) {
                Direction.LEFT -> nextX--
                Direction.UP -> nextY--
                Direction.RIGHT -> nextX++
                Direction.DOWN -> nextY++
            }
            if (nextX == 0)
                nextX = maxX
            if (nextX > maxX)
                nextX = 1
            if (nextY == 0)
                nextY = maxY
            if (nextY > maxY)
                nextY = 1

            blizzard.x = nextX
            blizzard.y = nextY
        }
    }

    // List of squares it's safe to stand on by turn
    val safetySquares = hashMapOf<Int, MutableList<Point>>()
    var safetyTurn = 0
    fun incrementSafetySquares(blizzards: List<Blizzard>) {
        moveBlizzards(blizzards)
        safetyTurn++

        val newSquares = mutableListOf<Point>()

        for (y in 1..maxY) {
            for (x in 1..maxX) {
                if (blizzards.filter { it.x == x && it.y == y }.count() == 0) {
                    newSquares.add(Point(x, y))
                }
            }
        }
        safetySquares[safetyTurn] = newSquares
    }

    fun printBlizzards(blizzards: List<Blizzard>) {
        for (y in 1..maxY) {
            for (x in 1..maxX) {
                val count = blizzards.filter { it.x == x && it.y == y}.count()
                if (count > 1)
                    print(count)
                else if (count == 1) {
                    val blizzard = blizzards.find { it.x == x && it.y == y}!!
                    print(when (blizzard.dir) {
                        Direction.UP -> '^'
                        Direction.DOWN -> 'v'
                        Direction.RIGHT -> '>'
                        Direction.LEFT -> '<'
                    })
                } else {
                    print('.')
                }
            }
            println()
        }
        println()
    }

    fun findPathBfs (blizzards: List<Blizzard>, x: Int, y: Int, goalX: Int, goalY: Int, turn: Int): Int {
//        val q = PriorityQueue<Triple<Int, Int, Int>> { t1, t2 -> (t1.third - t2.third) * 1_000 + t2.first + t2.second - t1.first - t1.second }
        val q = PriorityQueue<Triple<Int, Int, Int>> { t1, t2 -> (t1.third + abs(t1.first - goalX) + abs(t1.second - goalY)) - (t2.third + abs(t2.first - goalX) + abs(t2.second - goalY)) }

        q.add(Triple(x, y, turn))

        var maxTurn = turn - 1

        while (q.isNotEmpty()) {
            val next = q.remove()
            q.removeAll { it == next }
            if (next.third > maxTurn) {
                maxTurn = next.third
//                println("Turn $maxTurn - Queue size ${q.size} - Safety size ${safetySquares.values.sumOf { it.size }} - First X,Y ${next.first}, ${next.second}")
            }
            if (next.first == goalX && next.second == goalY)
                return next.third + 1

            // Add safety squares if we don't have them
            while (safetySquares[maxTurn + 1] == null) {
                incrementSafetySquares(blizzards)
            }

            // If we can move a direction next turn
            // RIGHT
            if (safetySquares[next.third + 1]?.contains(Point(next.first + 1, next.second)) == true)
                q.add(Triple(next.first + 1, next.second, next.third + 1))
            // DOWN
            if (safetySquares[next.third + 1]?.contains(Point(next.first, next.second + 1)) == true)
                q.add(Triple(next.first, next.second + 1, next.third + 1))
            // LEFT
            if (safetySquares[next.third + 1]?.contains(Point(next.first - 1, next.second)) == true)
                q.add(Triple(next.first - 1, next.second, next.third + 1))
            // UP
            if (safetySquares[next.third + 1]?.contains(Point(next.first, next.second - 1)) == true)
                q.add(Triple(next.first, next.second - 1, next.third + 1))
            // WAIT
            if (safetySquares[next.third + 1]?.contains(Point(next.first, next.second)) == true || (next.first == x && next.second == y))
                q.add(Triple(next.first, next.second, next.third + 1))
        }

        return Int.MIN_VALUE
    }

    fun part1(input: List<String>): Int {
        safetyTurn = 0
        safetySquares.clear()
        val blizzards = mutableListOf<Blizzard>()

        for (y in input.indices) {
            val line = input[y]
            for (x in line.indices) {
                when (line[x]) {
                    '<' -> blizzards.add(Blizzard(x, y, Direction.LEFT))
                    '^' -> blizzards.add(Blizzard(x, y, Direction.UP))
                    '>' -> blizzards.add(Blizzard(x, y, Direction.RIGHT))
                    'v' -> blizzards.add(Blizzard(x, y, Direction.DOWN))
                }
            }
        }

        maxX = input[0].length - 2
        maxY = input.size - 2

        val min = findPathBfs(
            blizzards = blizzards,
            x = 1,
            y = 0,
            goalX = maxX,
            goalY = maxY,
            0
        )
        return min
    }

    fun part2(input: List<String>): Int {
        safetyTurn = 0
        safetySquares.clear()
        val blizzards = mutableListOf<Blizzard>()

        for (y in input.indices) {
            val line = input[y]
            for (x in line.indices) {
                when (line[x]) {
                    '<' -> blizzards.add(Blizzard(x, y, Direction.LEFT))
                    '^' -> blizzards.add(Blizzard(x, y, Direction.UP))
                    '>' -> blizzards.add(Blizzard(x, y, Direction.RIGHT))
                    'v' -> blizzards.add(Blizzard(x, y, Direction.DOWN))
                }
            }
        }

        maxX = input[0].length - 2
        maxY = input.size - 2

        var min = findPathBfs(
            blizzards = blizzards,
            x = 1,
            y = 0,
            goalX = maxX,
            goalY = maxY,
            turn = 0
        )
        min = findPathBfs(
            blizzards = blizzards,
            x = maxX,
            y = maxY + 1,
            goalX = 1,
            goalY = 1,
            turn = min
        )
        min = findPathBfs(
            blizzards = blizzards,
            x = 1,
            y = 0,
            goalX = maxX,
            goalY = maxY,
            turn = min
        )
        return min
    }

    val testInput = readInputString("day24/test")
    val input = readInputString("day24/input")

    check(part1(testInput) == 18)
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")

    check(part2(testInput) == 54)
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}