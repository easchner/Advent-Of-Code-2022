package day07

import readInputString

data class File(val name: String, val size: Long)

class Directory(val parent: Directory?) {
    val children = mutableMapOf<String, Directory>()
    val files = mutableListOf<File>()
}

fun main() {
    fun getDirectorySize(directory: Directory): Long {
        var totalSize = directory.files.sumOf { it.size }
        for (c in directory.children) {
            totalSize += getDirectorySize(c.value)
        }
        return totalSize
    }

    fun getAllChildren(directory: Directory): List<Directory> {
        return directory.children.values.flatMap { getAllChildren(it) } + directory
    }

    fun traverseDirectory(directory: Directory, path: String): Directory {
        val dirs = path.split("/")
        var currentDir = directory
        for (d in dirs) {
            if (d.isNotEmpty())
                currentDir = currentDir.children[d]!!
        }
        return currentDir
    }

    fun readInput(root: Directory, input: List<String>) {
        var currentDirectory = root

        var currentLine = 0
        while (currentLine < input.size) {
            var line = input[currentLine]
            if (line.substring(0, 4) == "$ cd") {
                // Change directories
                val newDirectory = line.split(" ")[2]
                if (newDirectory[0] == '/') {
                    currentDirectory = traverseDirectory(root, newDirectory.substringAfter('/'))
                } else if (newDirectory == ".." && currentDirectory.parent != null) {
                    currentDirectory = currentDirectory.parent!!
                } else {
                    currentDirectory = traverseDirectory(currentDirectory, newDirectory)
                }
                currentLine++
            } else if (line.substring(0, 4) == "$ ls") {
                // Add children
                currentLine++
                line = input[currentLine]
                while (currentLine < input.size && line[0] != '$') {
                    if (line.substring(0, 3) == "dir") {
                        // Add directory as child
                        val newDirectory = Directory(currentDirectory)
                        currentDirectory.children[line.split(" ")[1]] = newDirectory
                    } else {
                        // Add file as child
                        val fileInfo = line.split(" ")
                        currentDirectory.files.add(File(fileInfo[1], fileInfo[0].toLong()))
                    }
                    currentLine++
                    if (currentLine < input.size) line = input[currentLine]
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        val root = Directory(null)
        readInput(root, input)

        val directories = getAllChildren(root)
        return directories.sumOf { it ->
            val size = getDirectorySize(it)
            if (size <= 100_000) size else 0
        }
    }

    fun part2(input: List<String>): Long {
        val root = Directory(null)
        readInput(root, input)

        val requiredSpace = 30_000_000 - (70_000_000 - getDirectorySize(root))
        val directories = getAllChildren(root).map {
            val size = getDirectorySize(it)
            if (size >= requiredSpace) size else Long.MAX_VALUE
        }
        return directories.minByOrNull { it }!!
    }

    val testInput = readInputString("day07/test")
    val input = readInputString("day07/input")

    check(part1(testInput) == 95_437L)
    println(part1(input))

    check(part2(testInput) == 24_933_642L)
    println(part2(input))
}