package day25

import readInputString
import java.lang.Math.abs
import java.lang.Math.pow
import java.util.PriorityQueue
import kotlin.math.pow
import kotlin.system.measureNanoTime

fun main() {
    fun snafuToDecimal(snafu: String): Long {
        var total = 0L
        for (i in snafu.indices) {
            val power = 5.0.pow(snafu.length - i.toDouble() - 1).toLong()
            total += when (snafu[i]) {
                '2' -> 2L * power
                '1' -> 1L * power
                '0' -> 0L
                '-' -> -1L * power
                '=' -> -2L * power
                else -> 0L
            }
        }
        return total
    }

    fun decimalToSnafu(decimal: Long): String {
        var remainder = decimal
        var snafu = ""

        // Find biggest power of 5 that fits
        var fives = 1L
        while (fives < decimal) {
            fives *= 5
        }

        fives /= 5

        while (fives >= 1) {
            // How many fives can we pull out ?
            val div = (remainder + fives / 2 * if (remainder > 0) 1 else -1) / fives
            snafu += when (div.toInt()) {
                0 -> "0"
                1 -> "1"
                2 -> "2"
                -1 -> "-"
                -2 -> "="
                else -> ""
            }
            remainder -= fives * div
            fives /= 5
        }

        return snafu
    }

    fun part1(input: List<String>): String {
        return decimalToSnafu(input.sumOf { snafuToDecimal(it) })
    }

    fun part2(input: List<String>): String {
        return ""
    }

    val testInput = readInputString("day25/test")
    val input = readInputString("day25/input")

    check(part1(testInput) == "2=-1=0")
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")

    check(part2(testInput) == "")
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}