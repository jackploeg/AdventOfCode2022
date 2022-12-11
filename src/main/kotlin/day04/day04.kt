package day04

import utitlities.readStringFile

fun main() {
    println(getNumberOfContainments("src/main/kotlin/day04/test.txt"))
    println(getNumberOfContainments("src/main/kotlin/day04/input.txt"))

    println(getNumberOfOverlaps("src/main/kotlin/day04/test.txt"))
    println(getNumberOfOverlaps("src/main/kotlin/day04/input.txt"))

}

fun getNumberOfContainments(fileName: String): Int {
    val assignmentPairs: List<Pair<IntRange, IntRange>> = readStringFile(fileName)
        .map { toRangePair(it) }
    return assignmentPairs.count { rangesContained(it) }
}

fun getNumberOfOverlaps(fileName: String): Int {
    val assignmentPairs: List<Pair<IntRange, IntRange>> = readStringFile(fileName)
        .map { toRangePair(it) }
    return assignmentPairs.count { rangesOverlap(it) }
}

fun toRangePair(input: String): Pair<IntRange, IntRange> {
    val ranges = input.split(",")
    val (start1, end1) = ranges[0].split("-")
    val range1 = start1.toInt().rangeTo(end1.toInt())
    val (start2, end2) = ranges[1].split("-")
    val range2 = start2.toInt().rangeTo(end2.toInt())
    return Pair(range1, range2)
}

fun rangesContained(ranges: Pair<IntRange, IntRange>): Boolean {
    return (ranges.first.contains(ranges.second.first) && ranges.first.contains(ranges.second.last))
            || (ranges.second.contains(ranges.first.first) && ranges.second.contains(ranges.first.last))
}

fun rangesOverlap(ranges: Pair<IntRange, IntRange>): Boolean {
    return ranges.first.contains(ranges.second.first)
            || ranges.first.contains(ranges.second.last)
            || ranges.second.contains(ranges.first.first)
            || ranges.second.contains(ranges.first.last)
}