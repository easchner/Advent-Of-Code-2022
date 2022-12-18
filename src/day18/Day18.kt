package day18

import readInputString
import java.lang.Integer.max
import java.lang.Math.abs
import kotlin.system.measureNanoTime

data class Point(val x: Int, val y: Int, val z: Int, var visited: Boolean = false)

fun main() {
    fun part1(input: List<String>): Int {
        val cubes = mutableListOf<Point>()
        var totalExposed = 0

        for (line in input) {
            // 2,2,2
            val nums = line.split(",").map { it.toInt() }
            cubes.add(Point(nums[0], nums[1], nums[2]))
        }

        for (cube in cubes) {
            val checks = listOf(-1, 1)
            for (check in checks) {
                // Check X
                if (cubes.filter { it.x == cube.x + check && it.y == cube.y && it.z == cube.z }.isEmpty()) {
                    totalExposed++
                }

                // Check Y
                if (cubes.filter { it.x == cube.x && it.y == cube.y  + check && it.z == cube.z }.isEmpty()) {
                    totalExposed++
                }

                // Check Z
                if (cubes.filter { it.x == cube.x && it.y == cube.y && it.z == cube.z + check }.isEmpty()) {
                    totalExposed++
                }
            }
        }

        return totalExposed
    }

    fun part2(input: List<String>): Int {
        val cubes = mutableListOf<Point>()

        for (line in input) {
            // 2,2,2
            val nums = line.split(",").map { it.toInt() }
            cubes.add(Point(nums[0], nums[1], nums[2]))
        }

        val minX = cubes.minOf { it.x } - 1
        val maxX = cubes.maxOf { it.x } + 1
        val minY = cubes.minOf { it.y } - 1
        val maxY = cubes.maxOf { it.y } + 1
        val minZ = cubes.minOf { it.z } - 1
        val maxZ = cubes.maxOf { it.z } + 1

        // Fill entire possible void with water
        val void = mutableListOf<Point>()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    void.add(Point(x, y, z))
                }
            }
        }

        // Remove from void all spaces occupied by cubes
        for (cube in cubes) {
            void.remove(cube)
        }

        // Traverse void from outer point
        val first = void.find { it.x == minX }
        val q = mutableListOf<Point>()
        q.add(first!!)

        while(q.isNotEmpty()) {
            val next = q.removeFirst()
            if (!next.visited) {
                next.visited = true
                val checks = listOf(-1, 1)
                for (check in checks) {
                    // Check X
                    var neighbor = void.find { it.x == next.x + check && it.y == next.y && it.z == next.z }
                    if (neighbor != null) {
                        q.add(neighbor)
                    }

                    // Check Y
                    neighbor = void.find { it.x == next.x && it.y == next.y + check && it.z == next.z }
                    if (neighbor != null) {
                        q.add(neighbor)
                    }

                    // Check Z
                    neighbor = void.find { it.x == next.x && it.y == next.y && it.z == next.z + check }
                    if (neighbor != null) {
                        q.add(neighbor)
                    }
                }
            }
        }

        var totalExposed = 0
        // Find all places where a void touches a cube
        for (v in void.filter { it.visited }) {
            for (c in cubes) {
                if (kotlin.math.abs(v.x - c.x) == 1 && v.y == c.y && v.z == c.z) {
                    totalExposed++
                }
                if (v.x == c.x && kotlin.math.abs(v.y - c.y) == 1  && v.z == c.z) {
                    totalExposed++
                }
                if (v.x == c.x && v.y == c.y && kotlin.math.abs(v.z - c.z) == 1 ) {
                    totalExposed++
                }
            }
        }

//        // Alternatively, find all exposed surfaces of the void the same way we did in part 1
//        // Remember to subtract out the outside of the cube, but that's easy math!
//        void.removeIf { !it.visited }
//        for (v in void) {
//            val checks = listOf(-1, 1)
//            for (check in checks) {
//                // Check X
//                if (void.filter { it.x == v.x + check && it.y == v.y && it.z == v.z }.isEmpty()) {
//                    totalExposed++
//                }
//
//                // Check Y
//                if (void.filter { it.x == v.x && it.y == v.y  + check && it.z == v.z }.isEmpty()) {
//                    totalExposed++
//                }
//
//                // Check Z
//                if (void.filter { it.x == v.x && it.y == v.y && it.z == v.z + check }.isEmpty()) {
//                    totalExposed++
//                }
//            }
//        }
//        val sx = maxX - minX + 1
//        val sy = maxY - minY + 1
//        val sz = maxZ - minZ + 1
//        val totalOutsideSides = (sx * sy + sx * sz + sy * sz) * 2
//        totalExposed -= totalOutsideSides

        return totalExposed
    }

    val testInput = readInputString("day18/test")
    val input = readInputString("day18/input")

    check(part1(testInput) == 64)
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")

    check(part2(testInput) == 58)
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}