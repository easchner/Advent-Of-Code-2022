package day17

import readInputString
import kotlin.system.measureNanoTime

/*
####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##
 */

fun main() {
    val rocks = mutableListOf<Array<Pair<Int, Int>>>()
    rocks.add(arrayOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3)))
    rocks.add(arrayOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, 2), Pair(2, 1)))
    rocks.add(arrayOf(Pair(0, 2), Pair(1, 2), Pair(2, 0), Pair(2, 1), Pair(2, 2)))
    rocks.add(arrayOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)))
    rocks.add(arrayOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1)))

    fun part1(input: List<String>): Int {
        val jets = input[0]
        val chamber = mutableListOf<Array<Boolean>>()
        chamber.add(Array(7) { true })
        chamber.add(Array(7) { false })
        chamber.add(Array(7) { false })
        chamber.add(Array(7) { false })

        var totalMoves = 0

        for (t in 0 until 2022) {
            // Add rock
            val rock = rocks[t % rocks.size]
            var height = chamber.indexOfLast { it.contains(true) } + rock.maxOf { it.first } + 4
            // Expand chamber if needed
            while (chamber.size < height + 1) {
                chamber.add(Array(7) { false })
            }

            var left = 2
            var falling = true

            // While rock is falling
            while (falling) {
                falling = false
                // Push rock
                val isLeft = jets[totalMoves++ % jets.length] == '<'
                if (isLeft) {
                    // Check if it clears wall
                    if (left >= 1) {
                        var movesLeft = true
                        for (p in rock) {
                            val newCoord = Pair(height - p.first, left + p.second - 1)
                            if (chamber[newCoord.first][newCoord.second]) {
                                movesLeft = false
                            }
                        }
                        if (movesLeft) {
                            left--
                        }
                    }
                } else {
                    // Check if it clears wall
                    if (left + rock.maxOf { it.second } < 6) {
                        var movesRight = true
                        for (p in rock) {
                            val newCoord = Pair(height - p.first, left + p.second + 1)
                            if (chamber[newCoord.first][newCoord.second]) {
                                movesRight = false
                            }
                        }
                        if (movesRight) {
                            left++
                        }
                    }
                }
                // Drop rock
                var drops = true
                for (p in rock) {
                    val newCoord = Pair(height - p.first - 1, p.second + left)
                    if (chamber[newCoord.first][newCoord.second]) {
                        drops = false
                    }
                }
                if (drops) {
                    height--
                    falling = true
                }
            }

            // After falling
            for (p in rock) {
                chamber[height - p.first][p.second + left] = true
            }
        }
        return chamber.indexOfLast { it.contains(true) }
    }

    fun part2(input: List<String>): Long {
        val jets = input[0]
        val chamber = mutableListOf<Array<Boolean>>()
        chamber.add(Array(7) { true })
        chamber.add(Array(7) { false })
        chamber.add(Array(7) { false })
        chamber.add(Array(7) { false })

        var totalMoves = 0L

        val table = hashMapOf<Long, Long>()
        for (t in 0L until 100_000) {
            val lastLoop = table[(totalMoves % jets.length) * 100_000L + t % rocks.size]
            if (lastLoop != null && chamber.indexOfLast { it.contains(true) }.toLong() % lastLoop == 0L) {
                println("After $t rocks we have done $totalMoves (index is ${totalMoves % jets.length}) moves for a height of ${chamber.indexOfLast { it.contains(true) }}")
            }
//            if (t % 1740 == 1180L) {
//                println("After $t rocks (on rock ${t % rocks.size}) we have done $totalMoves (index is ${totalMoves % jets.length}) moves for a height of ${chamber.indexOfLast { it.contains(true) }}")
//            }
            table[(totalMoves % jets.length) * 100_000L + t % rocks.size] = chamber.indexOfLast { it.contains(true) }.toLong()
            // Add rock
            val rock = rocks[(t % rocks.size).toInt()]
            var height = chamber.indexOfLast { it.contains(true) } + rock.maxOf { it.first } + 4
            // Expand chamber if needed
            while (chamber.size < height + 1) {
                chamber.add(Array(7) { false })
            }

            var left = 2
            var falling = true

            // While rock is falling
            while (falling) {
                falling = false
                // Push rock
                val isLeft = jets[(totalMoves++ % jets.length).toInt()] == '<'
                if (isLeft) {
                    // Check if it clears wall
                    if (left >= 1) {
                        var movesLeft = true
                        for (p in rock) {
                            val newCoord = Pair(height - p.first, left + p.second - 1)
                            if (chamber[newCoord.first][newCoord.second]) {
                                movesLeft = false
                            }
                        }
                        if (movesLeft) {
                            left--
                        }
                    }
                } else {
                    // Check if it clears wall
                    if (left + rock.maxOf { it.second } < 6) {
                        var movesRight = true
                        for (p in rock) {
                            val newCoord = Pair(height - p.first, left + p.second + 1)
                            if (chamber[newCoord.first][newCoord.second]) {
                                movesRight = false
                            }
                        }
                        if (movesRight) {
                            left++
                        }
                    }
                }
                // Drop rock
                var drops = true
                for (p in rock) {
                    val newCoord = Pair(height - p.first - 1, p.second + left)
                    if (chamber[newCoord.first][newCoord.second]) {
                        drops = false
                    }
                }
                if (drops) {
                    height--
                    falling = true
                }
            }

            // After falling
            for (p in rock) {
                chamber[height - p.first][p.second + left] = true
            }
        }
        return chamber.indexOfLast { it.contains(true) }.toLong()
    }

    val testInput = readInputString("day17/test")
    val input = readInputString("day17/input")

    check(part1(testInput) == 3_068)
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")

//    check(part2(testInput) == 0)
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}