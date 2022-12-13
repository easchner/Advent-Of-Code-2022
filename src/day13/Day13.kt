package day13

import readInputSpaceDelimited

fun main() {
    fun listCompare(a: String, b: String): Boolean? {
        var at = a
        var bt = b
        if (a.toIntOrNull() != null && b.toIntOrNull() != null) {
            if (a.toInt() < b.toInt())
                return true
            if (a.toInt() > b.toInt())
                return false
            return null
        }

        if (a.toIntOrNull() == null && b.toIntOrNull() != null) {
            bt = "[$b]"
        }
        if (a.toIntOrNull() != null && b.toIntOrNull() == null) {
            at = "[$a]"
        }

        if (a == "" && b != "")
            return false
        if (a != "" && b == "")
            return true
        if (a == "" && b == "")
            return null

        val aElements = mutableListOf<String>()
        val bElements = mutableListOf<String>()

        at = at.substring(1, at.length - 1)
        bt = bt.substring(1, bt.length - 1)

        while (at != "") {
            if (at[0] != '[') {
                aElements.add(at.substringBefore(","))
                at = if (!at.contains(','))
                    ""
                else
                    at.substringAfter(",")
            } else {
                var i = 1
                var brackets = 1
                while (brackets > 0 && i < at.length) {
                    brackets += when (at[i]) {
                        '[' -> 1
                        ']' -> -1
                        else -> 0
                    }
                    i++
                }
                aElements.add(at.substring(0, i))
                at = at.substring(i)
            }
        }

        while (bt != "") {
            if (bt[0] != '[') {
                bElements.add(bt.substringBefore(","))
                bt = if (!bt.contains(','))
                    ""
                else
                    bt.substringAfter(",")
            } else {
                var i = 1
                var brackets = 1
                while (brackets > 0 && i < bt.length) {
                    brackets += when (bt[i]) {
                        '[' -> 1
                        ']' -> -1
                        else -> 0
                    }
                    i++
                }
                bElements.add(bt.substring(0, i))
                bt = bt.substring(i)
            }
        }

        aElements.remove("")
        bElements.remove("")

        while (aElements.isNotEmpty() && bElements.isNotEmpty()) {
            val comp = listCompare(aElements.removeFirst(), bElements.removeFirst())
            if (comp != null)
                return comp
        }

        if (aElements.isEmpty() && bElements.isNotEmpty())
            return true

        if (aElements.isNotEmpty() && bElements.isEmpty())
            return false

        return null
    }

    fun part1(input: List<List<String>>): Int {
        return input.mapIndexed { index, list -> if(listCompare(list[0], list[1]) == true) index + 1 else 0 }.sum()
    }

    fun part2(input: List<List<String>>): Int {
        val cleanInput = mutableListOf<String>()
        for (i in input) {
            for (j in i) {
                cleanInput.add(j)
            }
        }

        cleanInput.add("[[2]]")
        cleanInput.add("[[6]]")

        cleanInput.sortWith(Comparator<String> { a, b ->
            when (listCompare(a, b)) {
                true -> -1
                false -> 1
                else -> 0
            }
        })

        return (cleanInput.indexOf("[[2]]") + 1) * (cleanInput.indexOf("[[6]]") + 1)
    }

    val testInput = readInputSpaceDelimited("day13/test")
    val input = readInputSpaceDelimited("day13/input")

    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 140)
    println(part2(input))
}