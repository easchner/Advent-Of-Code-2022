package day15

import readInputString
import kotlin.math.abs

data class Sensor(var x: Long, var y: Long, var maxDistance: Long)
data class Beacon(var x: Long, var y: Long)

fun main() {
    fun findMaxDistance(s: Sensor, b: Beacon) {
        val xDist = abs(s.x - b.x)
        val yDist = abs(s.y - b.y)
        s.maxDistance = xDist + yDist
    }

    fun spotsCovered(s: Sensor, row: Long): Pair<Long, Long>? {
        val yDist = abs(s.y - row)
        if (yDist > s.maxDistance) {
            return null
        }
        val xDist = s.maxDistance - yDist
        return (Pair(s.x - xDist, s.x + xDist))
    }

    fun part1(input: List<String>, row: Long): Long {
        val sensors = mutableListOf<Sensor>()
        val beacons = mutableSetOf<Beacon>()
        val regex = Regex("-*\\d+")

        for (line in input) {
            // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            val nums = regex.findAll(line).map { it.value }.toList()
            sensors.add(Sensor(nums[0].toLong(), nums[1].toLong(), 0))
            val beacon = Beacon(nums[2].toLong(), nums[3].toLong())
            beacons.add(beacon)
            findMaxDistance(sensors.last(), beacon)
        }

        val ranges = mutableListOf<Pair<Long, Long>>()
        for (s in sensors) {
            val covered = spotsCovered(s, row)
            if (covered != null) {
                ranges.add(covered)
            }
        }

        val newRanges = ranges.toMutableList()
        var overlaps = true

        while(overlaps) {
            overlaps = false
            for (i in ranges.indices) {
                for (j in i + 1 until ranges.size) {
                    //      1                  2              3            4
                    //  a------b              a-------b      a-b      a--------b
                    //      c-------d    c--------d       c-------d       c-d
                    val a = ranges[i].first
                    val b = ranges[i].second
                    val c = ranges[j].first
                    val d = ranges[j].second
                    if (a <= d && b >= c) {
                        val overLeft = Math.min(a, c)
                        val overRight = Math.max(b, d)
                        newRanges.remove(ranges[i])
                        newRanges.remove(ranges[j])
                        newRanges.add(Pair(overLeft, overRight))
                        overlaps = true
                        break
                    }
                }
            }
            ranges.clear()
            ranges.addAll(newRanges)
        }

        val beaconsOnRow = beacons.count { it.y == row }
        return ranges.sumOf { it.second - it.first + 1 } - beaconsOnRow
    }

    fun part2(input: List<String>, maxCoordinate: Long): Long {
        val sensors = mutableListOf<Sensor>()
        val beacons = mutableSetOf<Beacon>()
        val regex = Regex("-*\\d+")

        for (line in input) {
            // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            val nums = regex.findAll(line).map { it.value }.toList()
            sensors.add(Sensor(nums[0].toLong(), nums[1].toLong(), 0))
            val beacon = Beacon(nums[2].toLong(), nums[3].toLong())
            beacons.add(beacon)
            findMaxDistance(sensors.last(), beacon)
        }

        for (row in 0 until maxCoordinate) {
            val ranges = mutableListOf<Pair<Long, Long>>()
            for (s in sensors) {
                val covered = spotsCovered(s, row)
                if (covered != null) {
                    ranges.add(covered)
                }
            }

            val newRanges = ranges.toMutableList()
            var overlaps = true

            while (overlaps) {
                overlaps = false
                for (i in ranges.indices) {
                    for (j in i + 1 until ranges.size) {
                        //      1                  2              3            4
                        //  a------b              a-------b      a-b      a--------b
                        //      c-------d    c--------d       c-------d       c-d
                        val a = ranges[i].first
                        val b = ranges[i].second
                        val c = ranges[j].first
                        val d = ranges[j].second
                        if (a <= d && b >= c) {
                            val overLeft = Math.min(a, c)
                            val overRight = Math.max(b, d)
                            newRanges.remove(ranges[i])
                            newRanges.remove(ranges[j])
                            newRanges.add(Pair(overLeft, overRight))
                            overlaps = true
                            break
                        }
                    }
                }
                ranges.clear()
                ranges.addAll(newRanges)
            }
            if (ranges.size > 1) {
                return if (ranges[0].first < ranges[1].first) {
                    (ranges[0].second + 1) * 4_000_000 + row
                } else {
                    (ranges[1].second + 1) * 4_000_000 + row
                }
            }
        }
        return 0L
    }

    val testInput = readInputString("day15/test")
    val input = readInputString("day15/input")

    check(part1(testInput, 10L) == 26L)
    println(part1(input, 2_000_000L))

    check(part2(testInput, 20) == 56_000_011L)
    println(part2(input, 4_000_000))
}