package day01

import utitlities.readStringFile

fun main() {
    println(getMaxLoad("src/main/kotlin/day01/test.txt"))
    println(getMaxLoad("src/main/kotlin/day01/input.txt"))

    println(getTop3MaxLoad("src/main/kotlin/day01/test.txt"))
    println(getTop3MaxLoad("src/main/kotlin/day01/input.txt"))
}

fun getMaxLoad(fileName: String): Int {
    return readElfLoads(fileName).first()
}

fun getTop3MaxLoad(fileName: String): Int {
    return readElfLoads(fileName).take(3).reduce { sum, load -> sum + load }
}

fun readElfLoads(fileName: String): List<Int> {
    val loadSums = mutableListOf<Int>()
    val loads = readStringFile(fileName)

    var currentLoad = 0
    for (load in loads) {
        if (load.equals("")) {
            loadSums.add(loadSums.size, currentLoad)
            currentLoad = 0
        } else {
            currentLoad += Integer.valueOf(load)
        }
    }
    loadSums.add(loadSums.size, currentLoad)
    return loadSums.sortedDescending()
}

