package day07

import utitlities.readStringFile

fun main() {
    println(dirsLessOrEqual(100000, "src/main/kotlin/day07/test.txt"))
    println(dirsLessOrEqual(100000, "src/main/kotlin/day07/input.txt"))

    println(dirSizeForEnoughFreeSpace(70000000, 30000000, "src/main/kotlin/day07/test.txt"))
    println(dirSizeForEnoughFreeSpace(70000000, 30000000, "src/main/kotlin/day07/input.txt"))
}

fun dirsLessOrEqual(maxSize: Int, fileName: String): Int {
    val input = readStringFile(fileName)
    val tree = readTree(input)
    return tree.flattened().filter { it.getSize() <= maxSize }.sumOf { it.getSize() }
}

fun dirSizeForEnoughFreeSpace(totalSize: Int, neededFree: Int, fileName: String): Int {
    val input = readStringFile(fileName)
    val tree = readTree(input)
    val freeSpace = totalSize - tree.getSize()
    val needed = neededFree - freeSpace
    return tree.flattened().filter { it.getSize() >= needed }.sortedBy { it.getSize() }.first().getSize()
}

fun readTree(lines: List<String>): Directory {

    val root = Directory.create("/", null)
    var currentDir = root

    var i = 0
    do {
        val line = lines[i]
        if (line.startsWith("$")) {
            val commandLine = line.substring(2).split(" ")
            if (commandLine[0] == "cd") {
                if (commandLine[1] == "/") {
                    currentDir = root
                } else if (commandLine[1] == "..") {
                    if (currentDir != root) {
                        currentDir = currentDir.parent!!
                    }
                } else {
                    currentDir = currentDir.children.filter { it.name == commandLine[1] }.first() as Directory
                }
            } else if (commandLine[0] == "ls") {

            }
        } else {
            val entry = line.split(" ")
            if (entry[0] == "dir") {
                val newDir = Directory.create(entry[1], currentDir)
                currentDir.children.add(newDir)
            } else {
                val newFile = File(entry[1], currentDir, Integer.parseInt(entry[0]))
                currentDir.children.add(newFile)
            }
        }
        i++
    } while (i < lines.size)
    return root
}

open class Node(val name: String, val parent: Directory?)

class File(name: String, parent: Directory, val size: Int) : Node(name, parent)

class Directory(name: String, parent: Directory?, val children: MutableSet<Node>) : Node(name, parent) {
    companion object {
        fun create(name: String, parent: Directory?) = Directory(name, parent, HashSet<Node>())
    }

    fun getSize(): Int {
        return children.filter { it is File }.map { (it as File).size }.sum() + children.filter { it is Directory }
            .map { (it as Directory).getSize() }.sum()
    }

    fun flattened(): List<Directory> {
        val allRoles = mutableListOf<Directory>()
        this.flattenedTo(allRoles)
        return allRoles
    }

    fun flattenedTo(destination: MutableCollection<Directory>) {
        destination.add(this)
        children.filter { it is Directory }?.forEach { (it as Directory).flattenedTo(destination) }
    }
}

