package day20

import readInputString
import java.lang.Exception
import kotlin.system.measureNanoTime

data class WorkingNumber(val original: Int, val number: Long)

fun main() {
    fun moveItem(coords: MutableList<WorkingNumber>, index: Int) {
        val item = coords.find { it.original == index } ?: throw Exception()
        val oldIndex = coords.indexOf(item)
        var newIndex = (oldIndex + item.number) % (coords.size - 1)
        if (newIndex < 0) {
            newIndex += coords.size - 1
        }
        coords.removeAt(oldIndex)
        coords.add(newIndex.toInt(), item)
    }

    fun part1(input: List<String>): Long {
        val coords = input.mapIndexed { index, s -> WorkingNumber(index, s.toLong()) }.toMutableList()

        for (i in coords.indices) {
            moveItem(coords, i)
        }

        val zeroIndex = coords.indexOfFirst { it.number == 0L } ?: throw Exception()

        return coords[((zeroIndex + 1_000L) % coords.size).toInt()].number + coords[((zeroIndex + 2_000L) % coords.size).toInt()].number + coords[((zeroIndex + 3_000L) % coords.size).toInt()].number
    }

    fun part2(input: List<String>): Long {
        val coords = input.mapIndexed { index, s -> WorkingNumber(index, s.toLong() * 811_589_153L) }.toMutableList()

        for (x in 0 until 10) {
            for (i in coords.indices) {
                moveItem(coords, i)
            }
        }

        val zeroIndex = coords.indexOfFirst { it.number == 0L } ?: throw Exception()

        return coords[(zeroIndex + 1_000) % coords.size].number + coords[(zeroIndex + 2_000) % coords.size].number + coords[(zeroIndex + 3_000) % coords.size].number
    }

    val testInput = readInputString("day20/test")
    val input = readInputString("day20/input")

    check(part1(testInput) == 3L)
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")

    check(part2(testInput) == 1623178306L)
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}