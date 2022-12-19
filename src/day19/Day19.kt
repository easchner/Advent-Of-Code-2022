package day19

import readInputString
import java.lang.Integer.max
import kotlin.system.measureNanoTime

data class Blueprint(
    val label: Int,
    val oreOre: Int,
    val clayOre: Int,
    val obOre: Int,
    val obClay: Int,
    val geoOre: Int,
    val geoOb: Int
)

data class State(
    var turn: Int,
    var numOreBots: Int = 1,
    var numClayBots: Int = 0,
    var numObBots: Int = 0,
    var numGeoBots: Int = 0,
    var numOre: Int = 0,
    var numClay: Int = 0,
    var numOb: Int = 0,
    var numGeo: Int = 0
)

fun main() {
    var maxGeos = 0
    val visited = mutableSetOf<Int>()
    fun maxBlueprint(print: Blueprint, state: State) {
        if (state.turn <= 0) {
            maxGeos = max(maxGeos, state.numGeo)
            return
        }
        // Have we been at a transposition of this state?
        if (visited.contains(state.hashCode())) {
            return
        } else {
            visited.add(state.hashCode())
        }

        // How many geos can we possibly collect if we had unlimited resources
        var possibleGeos = state.numGeo + state.turn * state.numGeoBots
        for (t in state.turn downTo 0) {
            possibleGeos += t
        }
        if (maxGeos >= possibleGeos)
            return

        if (state.numOre >= print.geoOre && state.numOb >= print.geoOb && state.turn >= 2) {
            val newState = state.copy(
                turn = state.turn - 1,
                numGeoBots = state.numGeoBots + 1,
                numOre = state.numOre - print.geoOre + state.numOreBots,
                numOb = state.numOb - print.geoOb + state.numObBots,
                numClay = state.numClay + state.numClayBots,
                numGeo = state.numGeo + state.numGeoBots
            )
            maxBlueprint(print, newState)
        }
        if (state.numOre >= print.obOre && state.numClay >= print.obClay && state.turn >= 4 && state.numObBots < 10) {
            val newState = state.copy(
                turn = state.turn - 1,
                numObBots = state.numObBots + 1,
                numOre = state.numOre - print.obOre + state.numOreBots,
                numClay = state.numClay - print.obClay + state.numClayBots,
                numOb = state.numOb + state.numObBots,
                numGeo = state.numGeo + state.numGeoBots
            )
            maxBlueprint(print, newState)
        }
        if (state.numOre >= print.clayOre && state.turn >= 6 && state.numClayBots < 15) {
            val newState = state.copy(
                turn = state.turn - 1,
                numClayBots = state.numClayBots + 1,
                numOre = state.numOre - print.clayOre + state.numOreBots,
                numOb = state.numOb + state.numObBots,
                numClay = state.numClay + state.numClayBots,
                numGeo = state.numGeo + state.numGeoBots
            )
            maxBlueprint(print, newState)
        }
        if (state.numOre >= print.oreOre && state.turn >= 4 && state.numOreBots < 20) {
            val newState = state.copy(
                turn = state.turn - 1,
                numOreBots = state.numOreBots + 1,
                numOre = state.numOre - print.oreOre + state.numOreBots,
                numOb = state.numOb + state.numObBots,
                numClay = state.numClay + state.numClayBots,
                numGeo = state.numGeo + state.numGeoBots
            )
            maxBlueprint(print, newState)
        }

        val newState = state.copy(
            turn = state.turn - 1,
            numOre = state.numOre + state.numOreBots,
            numOb = state.numOb + state.numObBots,
            numClay = state.numClay + state.numClayBots,
            numGeo = state.numGeo + state.numGeoBots
        )
        maxBlueprint(print, newState)
    }

    fun part1(input: List<String>): Int {
        var qualityScore = 0
        val regex = Regex("-*\\d+")
        val prints = mutableListOf<Blueprint>()

        for (line in input) {
            // Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 11 clay. Each geode robot costs 2 ore and 7 obsidian.
            val nums = regex.findAll(line).map { it.value.toInt() }.toList()
            prints.add(Blueprint(nums[0], nums[1], nums[2], nums[3], nums[4], nums[5], nums[6]))
        }

        for (p in prints) {
            maxGeos = 0
            maxBlueprint(p, State(turn = 24))
            println("Best for print $p = $maxGeos")
            qualityScore += p.label * maxGeos
            visited.clear()
        }

        return qualityScore
    }

    fun part2(input: List<String>): Int {
        var qualityScore = 1
        val regex = Regex("-*\\d+")
        val prints = mutableListOf<Blueprint>()

        for (line in input) {
            // Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 11 clay. Each geode robot costs 2 ore and 7 obsidian.
            val nums = regex.findAll(line).map { it.value.toInt() }.toList()
            prints.add(Blueprint(nums[0], nums[1], nums[2], nums[3], nums[4], nums[5], nums[6]))
        }

        for (p in 0 until 3) {
            maxGeos = 0
            maxBlueprint(prints[p], State(turn = 32))
            println("Best for print $p = $maxGeos")
            qualityScore *= maxGeos
            visited.clear()
        }

        return qualityScore
    }

    val testInput = readInputString("day19/test")
    val input = readInputString("day19/input")

//    check(part1(testInput) == 33)
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")

//    check(part2(testInput) == 3_472)
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}