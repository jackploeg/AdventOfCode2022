package template

import utitlities.readStringFile

fun main() {
    println(xxx(100000, "src/main/kotlin/template/test.txt"))
}

fun xxx(maxSize: Int, fileName: String): Int {
    val input = readStringFile(fileName)
    return -1
}
