package day16

import readInputString
import java.util.PriorityQueue
import kotlin.math.max
import kotlin.math.pow
import kotlin.system.measureNanoTime

data class Connection (val label: String, var distance: Int)

data class Valve(
    val label: String,
    val flowRate: Int,
    val connections: MutableList<Connection>,
    var open: Boolean
)

fun main() {
    fun findShortestPath(graph: List<Valve>, start: String, target: String): Int {
        val q = PriorityQueue<Pair<Valve, Int>> { t1, t2 -> t1.second - t2.second}
        q.add(Pair(graph.find { it.label == start }!!, 0))

        while (q.isNotEmpty()) {
            val next = q.remove()
            // There's no connection - whooaaaOhhhhhOooohhhhaaaaa
            if (next.second > 30) {
                return -1
            }
            if (next.first.label == target) {
                return next.second
            }
            for (c in next.first.connections) {
                q.add(Pair(graph.find { it.label == c.label }!!, c.distance + next.second))
            }
        }

        return -1
    }

    fun getValveGraph(input: List<String>): List<Valve> {
        val valves = mutableListOf<Valve>()
        val regex = Regex("-*\\d+")
        val head = "AA" //input[0].substring(6, 8)

        // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        for (line in input) {
            valves.add(Valve(
                label = line.substring(6, 8),
                flowRate = regex.find(line)!!.value.toInt(),
                connections = line.substringAfter("valve").substringAfter(" ").split(", ").map {
                    Connection(it, 1)
                }.toMutableList(),
                open = false
            ))
        }

        var reduce = true
        while (reduce) {
            reduce = false
            for (v in valves) {
                // If it links to a valve with flow of 0, remove link and replace with bigger link
                val newConnections = mutableListOf<Connection>()
                for (c in v.connections) {
                    val connected = valves.find { it.label == c.label }!!
                    if (connected.flowRate == 0) {
                        for (n in connected.connections) {
                            newConnections.add(Connection(n.label, n.distance + c.distance))
                        }
                        reduce = true
                    } else {
                        newConnections.add(c)
                    }
                }
                v.connections.clear()
                v.connections.addAll(newConnections)

                // Clear out paths just back to self
                v.connections.removeIf { it.label == v.label }
            }

            // Clear out valves that are now skippable
            val newValves = mutableListOf<Valve>()
            for (v in valves) {
                val refs = valves.filter { r -> r.connections.map { c -> c.label }.contains(v.label) }.count()
                if (v.label == head || refs > 0) {
                    newValves.add(v)
                }
            }
            valves.clear()
            valves.addAll(newValves)

            // Pull out duplicates to same node
            for (v in valves) {
                val newConnections = mutableListOf<Connection>()
                for (c in v.connections) {
                    if (newConnections.count { it.label == c.label} == 0) {
                        val min = v.connections.filter { it.label == c.label }.minBy { it.distance }.distance
                        newConnections.add(Connection(c.label, min))
                    }
                }
                v.connections.clear()
                v.connections.addAll(newConnections)

                // Simplifies later logic if we just assume all bad valves are already open
                if (v.flowRate == 0) {
                    v.open = true
                }
            }
        }

        // For each pair of valves, store just the shortest path
        for (v in valves) {
            val newConnections = mutableListOf<Connection>()
            for (c in valves) {
                if (c.label != v.label && c.label != head) {
                    val shortest = findShortestPath(valves, v.label, c.label)
                    if (shortest > 0) {
                        newConnections.add(Connection(c.label, shortest))
                    }
                }
            }
            v.connections.clear()
            v.connections.addAll(newConnections)
        }

        return valves
    }

    val transpositionTable = hashMapOf<String, Int>()
    var bestSeen = 0
    fun traverse(graph: List<Valve>, head: String, time: Int, priorPoints: Int, direct: Boolean = false): Int {
        var maxOutput = 0
        val closed = graph.filter { !it.open }

        // End conditions, out of time, nothing left to open, can't possibly increase score
        if (time <= 0 || closed.isEmpty() || closed.sumOf { it.flowRate * (time - 1) } + priorPoints < bestSeen) {
            return 0
        }

        // Have we been in this same situation before?
        // We only care about our location, closed valves, and time left
        val key = closed.joinToString("") { it.label } + " " + head + " " + time.toString()
        val pts = transpositionTable[key]
        if (pts != null) {
            return pts
        }

        val current = graph.find { it.label == head }!!

        // If we're closed, try opening
        if (current.flowRate > 0 && !current.open) {
            current.open = true
            val thisPoints = (time - 1) * current.flowRate
            maxOutput = max(maxOutput, (thisPoints + traverse(graph, head, time - 1, thisPoints + priorPoints)))
            current.open = false
        }

        // If we took an express route directly here, don't traverse without opening
        if (!direct) {
            // Find all open targets (each closed valve with flow)
            val closedValves = graph.filter { !it.open && it.label != current.label }

            for (v in closedValves) {
                val shortest = current.connections.find { it.label == v.label }!!.distance
                // Do we even have time to bother?
                if (shortest + 1 < time) {
                    // If we were better off opening this one we can prune
                    if (current.open || (v.flowRate * (time - 1 - shortest)) > (current.flowRate * time - 1)) {
                        // If all remaining valves were opened immediately, could we possibly improve our max output?
                        val bestRemaining = closedValves.sumOf { it.flowRate * (time - shortest - 1) }
                        if (bestRemaining > maxOutput) {
                            maxOutput = max(maxOutput, traverse(graph, v.label, time - shortest, priorPoints, true))
                        }
                    }
                }
            }
        }

        transpositionTable[key] = maxOutput
        bestSeen = max(bestSeen, maxOutput)
        return maxOutput
    }

    fun part1(input: List<String>): Int {
        val graph = getValveGraph(input)
        val output = traverse(graph, "AA", 30, 0)
        return output
    }

    fun part2(input: List<String>): Int {
        val graph = getValveGraph(input).toMutableList()
        var maxOut = 0

        val head = graph.find { it.label == "AA" }
        graph.remove(head)
        graph.add(head!!)

        // Divide nodes into two groupings, run both independently and add
        for (combo in 0 until (2.0.pow((graph.size - 1).toDouble())).toInt() / 2) {
            for (i in 0 until graph.size) {
                graph[i].open = combo shr i and 1 == 1
            }
            graph.find { it.label == "AA" }!!.open = true
            var output = traverse(graph, "AA", 26, 0)
            bestSeen = 0

            for (n in graph) {
                n.open = !n.open
            }
            graph.find { it.label == "AA" }!!.open = true

            output += traverse(graph, "AA", 26, 0)
            bestSeen = 0

            maxOut = max(maxOut, output)
        }

        return maxOut
    }

    val testInput = readInputString("day16/test")
    val input = readInputString("day16/input")

    check(part1(testInput) == 1_651)
    transpositionTable.clear()
    val time1 = measureNanoTime { println(part1(input)) }
    println("Time for part 1 was ${"%,d".format(time1)} ns")
    transpositionTable.clear()

    check(part2(testInput) == 1_707)
    transpositionTable.clear()
    val time2 = measureNanoTime { println(part2(input)) }
    println("Time for part 2 was ${"%,d".format(time2)} ns")
}