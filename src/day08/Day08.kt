package day08

import readInputString
import java.lang.Integer.max

fun main() {
    fun part1(input: List<String>): Int {
        val trees = mutableListOf<MutableList<Int>>()
        var totalVisible = 0

        for (line in input) {
            trees.add(mutableListOf())
            for (i in line) {
                trees.last().add(i.digitToInt())
            }
        }

        for (r in trees.indices) {
            var tallest = -1
            for (c in trees[r].indices) {
                var visible = false
                val height = trees[r][c]
                // Test to the left
                if (height > tallest) {
                   tallest = height
                   visible = true
                }

                // Test to the right
                var shade: Int? = trees[r].subList(c + 1, trees[r].size).maxOrNull()
                if (shade == null) shade = -1
                if (shade < height) {
                    visible = true
                }

                // Test above
                shade = trees.mapIndexed { i, row -> if (i < r) row[c] else -1 }.maxOrNull()
                if (shade == null) shade = -1
                if (shade < height) {
                    visible = true
                }

                // Test below
                shade = trees.mapIndexed { i, row -> if (i > r) row[c] else -1 }.maxOrNull()
                if (shade == null) shade = -1
                if (shade < height) {
                    visible = true
                }
                if (visible)
                    totalVisible++
            }
        }

        return totalVisible
    }

    fun part2(input: List<String>): Int {
        val trees = mutableListOf<MutableList<Int>>()
        var bestView = 0

        for (line in input) {
            trees.add(mutableListOf())
            for (i in line) {
                trees.last().add(i.digitToInt())
            }
        }

        for (r in trees.indices) {
            for (c in trees[r].indices) {
                val height = trees[r][c]

                // Left
                var left = 0
                for (x in c - 1 downTo 0) {
                    if (trees[r][x] < height) {
                        left++
                    } else {
                        left++
                        break
                    }
                }

                // Right
                var right = 0
                for (x in c + 1 until trees[r].size) {
                    if (trees[r][x] < height) {
                        right++
                    } else {
                        right++
                        break
                    }
                }

                // Up
                var up = 0
                for (x in r - 1 downTo 0) {
                    if (trees[x][c] < height) {
                        up++
                    } else {
                        up++
                        break
                    }
                }

                // Down
                var down = 0
                for (x in r + 1 until trees.size) {
                    if (trees[x][c] < height) {
                        down++
                    } else {
                        down++
                        break
                    }
                }

                val scenic = left * right * up * down
                bestView = max(bestView, scenic)
            }
        }

        return bestView
    }

    val testInput = readInputString("day08/test")
    val input = readInputString("day08/input")

    check(part1(testInput) == 21)
    println(part1(input))

    check(part2(testInput) == 8)
    println(part2(input))
}