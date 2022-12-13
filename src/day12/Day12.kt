package day12

import readInputString
import java.lang.Integer.min
import kotlin.system.measureNanoTime

fun main() {
    fun buildMinMountain(mountain: Array<Array<Int>>, ey: Int, ex: Int): Array<Array<Int>> {
        val minMountain = Array(mountain.size) { Array(mountain[0].size) { Int.MAX_VALUE - 10 } }
        minMountain[ey][ex] = 0

        var changed = true

        while (changed) {
            changed = false
            for (y in mountain.indices) {
                for (x in mountain[y].indices) {
                    // UP
                    if (y > 0 && mountain[y-1][x] <= mountain[y][x] + 1 && minMountain[y-1][x] + 1 < minMountain[y][x]) {
                        minMountain[y][x] = minMountain[y-1][x] + 1
                        changed = true
                    }
                    // DOWN
                    if (y < mountain.size - 1 && mountain[y+1][x] <= mountain[y][x] + 1 && minMountain[y+1][x] + 1 < minMountain[y][x]) {
                        minMountain[y][x] = minMountain[y+1][x] + 1
                        changed = true
                    }
                    // LEFT
                    if (x > 0 && mountain[y][x-1] <= mountain[y][x] + 1 && minMountain[y][x-1] + 1 < minMountain[y][x]) {
                        minMountain[y][x] = minMountain[y][x-1] + 1
                        changed = true
                    }
                    // RIGHT
                    if (x < mountain[0].size - 1 && mountain[y][x+1] <= mountain[y][x] + 1 && minMountain[y][x+1] + 1 < minMountain[y][x]) {
                        minMountain[y][x] = minMountain[y][x+1] + 1
                        changed = true
                    }
                }
            }
        }
        return minMountain
    }

    data class qElement (val y: Int, val x: Int, val dist: Int)

    fun bfs(mountain: Array<Array<Int>>, startLocations: List<qElement>, ey: Int, ex: Int): Int {
        val queue = mutableListOf<qElement>()
        val visited = Array(mountain.size) { Array(mountain[0].size ) { false } }
        for (e in startLocations) {
            queue.add(qElement(e.y, e.x, 0))
            visited[e.y][e.x] = true
        }

        while (queue.isNotEmpty()) {
            val n = queue.removeFirst()
            // UP
            if (n.y > 0 && mountain[n.y-1][n.x] <= mountain[n.y][n.x] + 1 && !visited[n.y-1][n.x]) {
                if (n.y - 1 == ey && n.x == ex)
                    return n.dist + 1
                queue.add(qElement(n.y - 1, n.x, n.dist + 1))
                visited[n.y-1][n.x] = true
            }
            // DOWN
            if (n.y < mountain.size - 1 && mountain[n.y+1][n.x] <= mountain[n.y][n.x] + 1 && !visited[n.y+1][n.x]) {
                if (n.y + 1 == ey && n.x == ex)
                    return n.dist + 1
                queue.add(qElement(n.y + 1, n.x, n.dist + 1))
                visited[n.y+1][n.x] = true
            }
            // LEFT
            if (n.x > 0 && mountain[n.y][n.x-1] <= mountain[n.y][n.x] + 1 && !visited[n.y][n.x-1]) {
                if (n.y == ey && n.x - 1 == ex)
                    return n.dist + 1
                queue.add(qElement(n.y, n.x - 1, n.dist + 1))
                visited[n.y][n.x-1] = true
            }
            // RIGHT
            if (n.x < mountain[0].size - 1 && mountain[n.y][n.x+1] <= mountain[n.y][n.x] + 1 && !visited[n.y][n.x+1]) {
                if (n.y == ey && n.x + 1 == ex)
                    return n.dist + 1
                queue.add(qElement(n.y, n.x + 1, n.dist + 1))
                visited[n.y][n.x+1] = true
            }
        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val mountain = Array<Array<Int>>(input.size) { Array(input[0].length) { 0 } }
        var sy = 0
        var sx = 0
        var ey = 0
        var ex = 0

        for (y in input.indices) {
            for (x in 0 until input[y].length) {
                if (input[y][x] == 'S') {
                    sy = y
                    sx = x
                    mountain[y][x] = 0
                } else if (input[y][x] == 'E') {
                    ey = y
                    ex = x
                    mountain[y][x] = 25
                } else {
                    mountain[y][x] = input[y][x] - 'a'
                }
            }
        }

        var minMountain = buildMinMountain(mountain, ey, ex)
        val minMountainTime = measureNanoTime { minMountain = buildMinMountain(mountain, ey, ex) }

        var bfsAnswer = bfs(mountain, listOf(qElement(sy, sx, 0)), ey, ex)
        val bfsTime = measureNanoTime { bfsAnswer = bfs(mountain, listOf(qElement(sy, sx, 0)), ey, ex) }

        println("=== PART ONE ===")
        println("  MinPathMountain Answer = ${minMountain[sy][sx]}")
        println("  BFS Answer             = $bfsAnswer")
        println("  MinPathMountain Time   = ${"%,d".format(minMountainTime).padStart(12)} ns")
        println("  BFS Time               = ${"%,d".format(bfsTime).padStart(12)} ns")
        return minMountain[sy][sx]
    }

    fun part2(input: List<String>): Int {
        val mountain = Array<Array<Int>>(input.size) { Array(input[0].length) { 0 } }
        var ey = 0
        var ex = 0
        var starts = mutableListOf<qElement>()

        for (y in input.indices) {
            for (x in 0 until input[y].length) {
                if (input[y][x] == 'S' || input[y][x] == 'a') {
                    starts.add(qElement(y, x, 0))
                    mountain[y][x] = 0
                } else if (input[y][x] == 'E') {
                    ey = y
                    ex = x
                    mountain[y][x] = 25
                } else {
                    mountain[y][x] = input[y][x] - 'a'
                }
            }
        }

        var minPath = Int.MAX_VALUE
        val minMountainTime = measureNanoTime {
            val minMountain = buildMinMountain(mountain, ey, ex)

            for (y in input.indices) {
                for (x in 0 until input[y].length) {
                    if (input[y][x] == 'a' || input[y][x] == 'S') {
                        minPath = min(minPath, minMountain[y][x])
                    }
                }
            }
        }

        var bfsAnswer = Int.MAX_VALUE
        val bfsTime = measureNanoTime {
            bfsAnswer = bfs(mountain, starts, ey, ex)
        }

        println("=== PART TWO ===")
        println("  MinPathMountain Answer = $minPath")
        println("  BFS Answer             = $bfsAnswer")
        println("  MinPathMountain Time   = ${"%,d".format(minMountainTime).padStart(12)} ns")
        println("  BFS Time               = ${"%,d".format(bfsTime).padStart(12)} ns")
        return minPath
    }

    val testInput = readInputString("day12/test")
    val input = readInputString("day12/input")

    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}