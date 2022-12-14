package day14

import readInputString
import java.lang.Integer.max

fun main() {
    fun buildCave(input: List<String>, addFloor: Boolean): Array<Array<Int>> {
        val cave = Array(1_000) { Array(1_000) { 0 } }
        var lowest = 0

        for (line in input) {
            val pts = line.split(" -> ").toMutableList()
            val firstPt = pts.removeFirst().split(",")
            var fx = firstPt[0].toInt()
            var fy = firstPt[1].toInt()
            for (pt in pts) {
               val secondPt = pt.split(",").toMutableList()
                val sx = secondPt[0].toInt()
                val sy = secondPt[1].toInt()

                while (fx < sx) {
                    cave[fx][fy] = 1
                    fx++
                }
                while (fx > sx) {
                    cave[fx][fy] = 1
                    fx--
                }
                while (fy < sy) {
                    cave[fx][fy] = 1
                    fy++
                }
                while (fy > sy) {
                    cave[fx][fy] = 1
                    fy--
                }
                cave[sx][sy] = 1
                lowest = max(lowest, sy)
            }
        }

        if (addFloor) {
            for (x in 0..999) {
                cave[x][lowest+2] = 1
            }
        }

        return cave
    }

    fun pourSand(cave: Array<Array<Int>>): Int {
        var totalSand = 0
        var moreSand = true

        while (moreSand) {
            moreSand = false

            var x = 500
            var y = 0

            while (x in 1..998 && y+1 in 0..999 && (cave[x][y+1] == 0 || cave[x-1][y+1] == 0 || cave[x+1][y+1] == 0)) {
                y++
                x = when {
                    cave[x][y] == 0 -> x
                    cave[x-1][y] == 0 -> x - 1
                    else -> x + 1
                }
            }

            if (x in 1..998 && y in 1..998) {
                cave[x][y] = 2
                moreSand = true
                totalSand++
            }
        }

        return totalSand
    }

    fun part1(input: List<String>): Int {
        val cave = buildCave(input, false)
        return pourSand(cave)
    }

    fun part2(input: List<String>): Int {
        val cave = buildCave(input, true)
        return pourSand(cave) + 1
    }

    val testInput = readInputString("day14/test")
    val input = readInputString("day14/input")

    check(part1(testInput) == 24)
    println(part1(input))

    check(part2(testInput) == 93)
    println(part2(input))
}