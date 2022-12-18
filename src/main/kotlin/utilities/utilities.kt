package utilities

import java.io.File

fun readNumbersFile(fileName: String): List<Int> {
    return File(fileName)
        .readLines()
        .map { it.toInt() }
}

fun readStringFile(fileName: String): List<String> {
    return File(fileName)
        .readLines()
}


