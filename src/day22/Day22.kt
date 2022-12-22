package day22

import readInputSpaceDelimited
import kotlin.system.measureNanoTime

fun main() {
    fun mapNext(map: Array<Array<Char>>, x: Int, y: Int, dir: Int): Pair<Int, Int> {
        var nextX = x
        var nextY = y

        when (dir) {
            0 -> nextX++
            1 -> nextY++
            2 -> nextX--
            3 -> nextY--
        }

        if (nextY < 0)
            nextY = map.size - 1
        else if (nextY >= map.size)
            nextY = 0
        else if (nextX < 0)
            nextX = map[nextY].size - 1
        else if (nextX >= map[nextY].size)
            nextX = 0

        while (map[nextY][nextX] == ' ') {
            when (dir) {
                0 -> nextX++
                1 -> nextY++
                2 -> nextX--
                3 -> nextY--
            }

            if (nextY < 0)
                nextY = map.size - 1
            else if (nextY >= map.size)
                nextY = 0
            else if (nextX < 0)
                nextX = map[nextY].size - 1
            else if (nextX >= map[nextY].size)
                nextX = 0
        }

        if (map[nextY][nextX] == '#')
            return Pair(x, y)
        return Pair(nextX, nextY)
    }

    val xA = 0 until 50
    val xB = 50 until 100
    val xC = 100 until 150

    val yA = 0 until 50
    val yB = 50 until 100
    val yC = 100 until 150
    val yD = 150 until 200

    /*
      A B C
    A   1 2
    B   3
    C 4 5
    D 6
 */

    fun mapNextCube(map: Array<Array<Char>>, x: Int, y: Int, dir: Int): Triple<Int, Int, Int> {
        var nextX = x
        var nextY = y
        var nextD = dir

        when (dir) {
            0 -> nextX++
            1 -> nextY++
            2 -> nextX--
            3 -> nextY--
        }

        if (nextY < 0)
            nextY = map.size - 1
        else if (nextY >= map.size)
            nextY = 0
        else if (nextX < 0)
            nextX = map[nextY].size - 1
        else if (nextX >= map[nextY].size)
            nextX = 0

        if (map[nextY][nextX] == ' ') {
            // If up on 1, move to face 6. Up is now right
            if (y in yA && x in xB && nextY in yD && nextX in xB) {
                nextX = 0
                nextY = x - 50 + 150
                nextD = 0
            }
            // If left on 1, move to face 4. Left is now right
            else if (y in yA && x in xB && nextY in yA && nextX in xA) {
                nextX = 0
                nextY = 49 - y + 100
                nextD = 0
            }
            // If up on 2, move to face 6. Up is still up
            else if (y in yA && x in xC && nextY in yD && nextX in xC) {
                nextX = x - 100
                nextY = 199
                nextD = 3
            }
            // If right on 2, move to face 5. Right is now left
            else if (y in yA && x in xC && nextY in yA && nextX in xA) {
                nextX = 99
                nextY = 49 - y + 100
                nextD = 2
            }
            // If down on 2, move to face 3. Down is now left
            else if (y in yA && x in xC && nextY in yB && nextX in xC) {
                nextX = 99
                nextY = x - 100 + 50
                nextD = 2
            }
            // If left on 3, move to face 4. Left is now down
            else if (y in yB && x in xB && nextY in yB && nextX in xA) {
                nextX = y - 50
                nextY = 100
                nextD = 1
            }
            // If right on 3, move to face 2. Right is now up
            else if (y in yB && x in xB && nextY in yB && nextX in xC) {
                nextX = y - 50 + 100
                nextY = 49
                nextD = 3
            }
            // If left on 4, move to face 1. Left is now right
            else if (y in yC && x in xA && nextY in yC && nextX in xC) {
                nextX = 50
                nextY = 149 - y
                nextD = 0
            }
            // If up on 4, move to face 3. Up is now right
            else if (y in yC && x in xA && nextY in yB && nextX in xA) {
                nextX = 50
                nextY = x - 0 + 50
                nextD = 0
            }
            // If down on 5, move to face 6. Down is now left
            else if (y in yC && x in xB && nextY in yD && nextX in xB) {
                nextX = 49
                nextY = x - 50 + 150
                nextD = 2
            }
            // If right on 5, move to face 2. Right is now left
            else if (y in yC && x in xB && nextY in yC && nextX in xC) {
                nextX = 149
                nextY = 149 - y
                nextD = 2
            }
            // If left on 6, move to face 1. Left is now down  ** Does not occur
            else if (y in yD && x in xA && nextY in yD && nextX in xC) {
                nextX = y - 150 + 50
                nextY = 0
                nextD = 1
            }
            // If right on 6, move to face 5. Right is now up
            else if (y in yD && x in xA && nextY in yD && nextX in xB) {
                nextX = y - 150 + 50
                nextY = 149
                nextD = 3
            }
            // If down on 6, move to face 2. Down is still down
            else if (y in yD && x in xA && nextY in yA && nextX in xA) {
                nextX = x + 100
                nextY = 0
                nextD = 1
            }
        }

        if (map[nextY][nextX] == '#')
            return Triple(x, y, dir)
        return Triple(nextX, nextY, nextD)
    }

    fun part1(input: List<List<String>>): Int {
        val map = Array(200) { Array(150) { ' ' } }
        val distances = mutableListOf<Int>()
        val directions = mutableListOf<Char>()
        val regexInt = Regex("-*\\d+")
        val regexDir = Regex("[A-Z]")

        // Read map
        for (y in input[0].indices) {
            for (x in input[0][y].indices) {
                map[y][x] = input[0][y][x]
            }
        }

        // Read instructions
        distances.addAll(regexInt.findAll(input[1][0]).map { it.value.toInt() })
        directions.addAll(regexDir.findAll(input[1][0]).map { it.value[0] })

        // Iterate through map
        var curX = 0
        var curY = 0
        var curD = 0

        while(distances.isNotEmpty()) {
            val dist = distances.removeFirst()
            for (i in 0 until dist) {
                val newCoords = mapNext(map, curX, curY, curD)
                curX = newCoords.first
                curY = newCoords.second
            }
            if (directions.isNotEmpty()) {
                val dir = directions.removeFirst()
                curD = if (dir == 'R')
                    (curD + 1) % 4
                else {
                    (curD + 3) % 4
                }
            }
        }

        return (curY + 1) * 1_000 + (curX + 1) * 4 + curD
    }

    fun part2(input: List<List<String>>): Int {
        val map = Array(200) { Array(150) { ' ' } }
        val distances = mutableListOf<Int>()
        val directions = mutableListOf<Char>()
        val regexInt = Regex("-*\\d+")
        val regexDir = Regex("[A-Z]")

        // Read map
        for (y in input[0].indices) {
            for (x in input[0][y].indices) {
                map[y][x] = input[0][y][x]
            }
        }

        // Read instructions
        distances.addAll(regexInt.findAll(input[1][0]).map { it.value.toInt() })
        directions.addAll(regexDir.findAll(input[1][0]).map { it.value[0] })

        // Iterate through map
        var curX = 50
        var curY = 0
        var curD = 0

        while(distances.isNotEmpty()) {
            val dist = distances.removeFirst()
            for (i in 0 until dist) {
                val newCoords = mapNextCube(map, curX, curY, curD)
                curX = newCoords.first
                curY = newCoords.second
                curD = newCoords.third
            }
            if (directions.isNotEmpty()) {
                val dir = directions.removeFirst()
                curD = if (dir == 'R')
                    (curD + 1) % 4
                else {
                    (curD + 3) % 4
                }
            }
        }

        return (curY + 1) * 1_000 + (curX + 1) * 4 + curD
    }

    val testInput = readInputSpaceDelimited("day22/test")
    val input = readInputSpaceDelimited("day22/input")

    check(part1(testInput) == 6032)
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")

//    check(part2(testInput) == 5031)
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}