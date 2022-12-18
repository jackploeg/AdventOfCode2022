package day06

import utilities.readStringFile

fun main() {
    findmarker(4, "src/main/kotlin/day06/test.txt")
    findmarker(4, "src/main/kotlin/day06/input.txt")
    findmarker(14, "src/main/kotlin/day06/test.txt")
    findmarker(14, "src/main/kotlin/day06/input.txt")
}

fun findmarker(markersize: Int, fileName: String) {
    val input = readStringFile(fileName)
    for(line in input) {
        val marker: ArrayDeque<Char> = ArrayDeque(markersize)
        val lineChars = line.toCharArray()
        for (i in 1..markersize) {
            marker.add(lineChars[i-1])
        }
        var markerEnd = markersize
        while (!allCharsDiffer(marker)) {
            val c = lineChars[markerEnd]
            marker.removeFirst()
            marker.add(c)
            markerEnd++
        }
        println(marker)
        println(markerEnd)
    }
}

fun allCharsDiffer(chars: ArrayDeque<Char>): Boolean =
    chars.distinct().size == chars.size

