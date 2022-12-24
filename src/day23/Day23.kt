package day23

import readInputString
import kotlin.system.measureNanoTime

data class Point(val x: Int, val y: Int)

fun main() {
    fun expandGrove(grove: MutableList<MutableList<Char>>) {
        // Expand top
        if (grove.first().contains('#')) {
            grove.add(0, MutableList(grove[0].size) { '.' })
        }
        // Expand bottom
        if (grove.last().contains('#')) {
            grove.add(MutableList(grove[0].size) { '.' })
        }
        // Expand left
        if (grove.map { it.first() }.contains('#')) {
            for (l in grove) {
                l.add(0, '.')
            }
        }
        // Expand right
        if (grove.map { it.last() }.contains('#')) {
            for (l in grove) {
                l.add('.')
            }
        }
    }

    fun findDestinations(grove: MutableList<MutableList<Char>>, round: Int): MutableList<Pair<Point, Point>> {
        val destinations = mutableListOf<Pair<Point, Point>>()

        for (y in grove.indices) {
            for (x in grove[y].indices) {
                // If it's an elf
                if (grove[y][x] == '#') {
                    // Check to see if it has no neighbors
                    var count = 0
                    for (i in (y-1)..(y+1)) {
                        for (j in (x-1)..(x+1)) {
                            if (grove[i][j] == '#') {
                                count++
                            }
                        }
                    }
                    // If it has neighbors
                    if (count > 1) {
                        var moved = false
                        for (o in 0 until 4) {
                            if (!moved) {
                                // N S W E
                                when ((o + round) % 4) {
                                    0 -> {
                                        // Check North
                                        var occupied = false
                                        for (i in (x - 1)..(x + 1)) {
                                            if (grove[y - 1][i] == '#')
                                                occupied = true
                                        }
                                        if (!occupied) {
                                            destinations.add(Pair(Point(x, y), Point(x, y - 1)))
                                            moved = true
                                        }
                                    }
                                    1 -> {
                                        // Check South
                                        var occupied = false
                                        for (i in (x - 1)..(x + 1)) {
                                            if (grove[y + 1][i] == '#')
                                                occupied = true
                                        }
                                        if (!occupied) {
                                            destinations.add(Pair(Point(x, y), Point(x, y + 1)))
                                            moved = true
                                        }
                                    }
                                    2 -> {
                                        // Check West
                                        var occupied = false
                                        for (i in (y - 1)..(y + 1)) {
                                            if (grove[i][x - 1] == '#')
                                                occupied = true
                                        }
                                        if (!occupied) {
                                            destinations.add(Pair(Point(x, y), Point(x - 1, y)))
                                            moved = true
                                        }
                                    }
                                    3 -> {
                                        // Check East
                                        var occupied = false
                                        for (i in (y - 1)..(y + 1)) {
                                            if (grove[i][x + 1] == '#')
                                                occupied = true
                                        }
                                        if (!occupied) {
                                            destinations.add(Pair(Point(x, y), Point(x + 1, y)))
                                            moved = true
                                        }
                                    }
                                } // when dir
                            } // !moved
                        } // for each dir
                    }
                }
            }
        }

        // Check if two elves want to move to same destination
        for (dest in destinations.map { it.second }.toSet()) {
            if (destinations.count { it.second == dest } > 1) {
                destinations.removeAll { it.second == dest }
            }
        }

        return destinations
    }

    fun moveToDestinations(grove: MutableList<MutableList<Char>>, destinations: MutableList<Pair<Point, Point>>) {
        // Move all the elves
        for (dest in destinations) {
            grove[dest.first.y][dest.first.x] = '.'
            grove[dest.second.y][dest.second.x] = '#'
        }
    }

    fun part1(input: List<String>): Int {
        val grove = mutableListOf<MutableList<Char>>()

        for (line in input) {
            grove.add(line.map { it }.toMutableList())
        }

        // Run 10 rounds
        for (i in 0 until 10) {
            expandGrove(grove)
            val destinations = findDestinations(grove, i)
            moveToDestinations(grove, destinations)
        }

        // Find rectangle
        var minY = Int.MAX_VALUE
        var minX = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE
        var maxX = Int.MIN_VALUE
        for (y in grove.indices) {
            if (grove[y].contains('#')) {
                minY = y
                break
            }
        }
        for (y in grove.indices.reversed()) {
            if (grove[y].contains('#')) {
                maxY = y
                break
            }
        }
        for (x in grove.first().indices) {
            if (grove.map { it[x] }.contains('#')) {
                minX = x
                break
            }
        }
        for (x in grove.first().indices.reversed()) {
            if (grove.map { it[x] }.contains('#')) {
                maxX = x
                break
            }
        }

        // Count blanks
        var blanks = 0
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (grove[y][x] == '.')
                    blanks++
            }
        }

        return blanks
    }

    fun part2(input: List<String>): Int {
        val grove = mutableListOf<MutableList<Char>>()

        for (line in input) {
            grove.add(line.map { it }.toMutableList())
        }

        var round = 0
        while (true) {
            expandGrove(grove)
            val destinations = findDestinations(grove, round)
            round++
            if (destinations.isEmpty()) {
                return round
            }
            moveToDestinations(grove, destinations)
        }
    }

    val testInput = readInputString("day23/test")
    val input = readInputString("day23/input")

    check(part1(testInput) == 110)
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")

    check(part2(testInput) == 20)
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}