package day12

import readInputString
import java.lang.Integer.min

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

        val minMountain = buildMinMountain(mountain, ey, ex)
        return minMountain[sy][sx]
    }

    fun part2(input: List<String>): Int {
        val mountain = Array<Array<Int>>(input.size) { Array(input[0].length) { 0 } }
        var ey = 0
        var ex = 0

        for (y in input.indices) {
            for (x in 0 until input[y].length) {
                if (input[y][x] == 'S') {
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

        val minMountain = buildMinMountain(mountain, ey, ex)

        var minPath = Int.MAX_VALUE
        for (y in input.indices) {
            for (x in 0 until input[y].length) {
                if (input[y][x] == 'a' || input[y][x] == 'S') {
                    minPath = min(minPath, minMountain[y][x])
                }
            }
        }

        return minPath
    }

    val testInput = readInputString("day12/test")
    val input = readInputString("day12/input")

    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}