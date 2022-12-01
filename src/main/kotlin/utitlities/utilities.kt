package utitlities

import java.io.File

fun readNumbersFile(fileName: String): List<Int> {
    val numbers = File(fileName)
        .readLines()
        .map { it.toInt() }
    return numbers
}

fun readStringFile(fileName: String): List<String> {
    return File(fileName)
        .readLines()
}


