package day24

import utilities.readStringFile
import java.util.*

data class State(val x: Int, val y: Int, val t: Int, val reachEnd: Boolean, val reachStart: Boolean)

fun main() {

    calculatePath(100, "src/main/kotlin/day24/test.txt")
    calculatePath(1000, "src/main/kotlin/day24/input.txt")

}

fun calculatePath(maxTime: Int, fileName: String) {

    val input = readStringFile(fileName)
    val (row, column) = input.size - 2 to input[0].length - 2
    // pre-fill 1000 consecutive grid states
    val badCells = (0..maxTime).associateWith { time ->
        buildSet {
            for (i in 0 until row)
                for (j in 0 until column)
                    when (input[i + 1][j + 1]) {
                        '>' -> add(i to (j + time) % column)
                        'v' -> add((i + time) % row to j)
                        '<' -> add(i to (j - time).mod(column))
                        '^' -> add((i - time).mod(row) to j)
                    }
        }
    }
    val stateQueue: Queue<State> = LinkedList()
    val seen = mutableSetOf<State>()
    stateQueue.add(State(x = 0, y = 1, t = 0, reachEnd = false, reachStart = false))
    var part1complete = false
    while (stateQueue.isNotEmpty()) {
        val state = stateQueue.poll()
        var (x, y, time, reachedEnd, reachedStart) = state
        if (x !in input.indices || y !in input[0].indices || input[x][y] == '#')
            continue
        if (x == input.lastIndex) {
            reachedEnd = true
            if (reachedStart) {
                println("Part 2: $time")
                return
            } else if (!part1complete)
                println("Part 1: $time")
            part1complete = true
        }
        if (x == 0 && reachedEnd)
            reachedStart = true
        if (state in seen)
            continue
        seen += state
        listOf(0 to 0, 0 to 1, 0 to -1, 1 to 0, -1 to 0).forEach { (dx, dy) ->
            if (x - 1 + dx to y - 1 + dy !in badCells[time + 1]!!)
                stateQueue.add(State(x + dx, y + dy, time + 1, reachedEnd, reachedStart))
        }
    }
}

