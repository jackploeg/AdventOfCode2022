package day05

import utitlities.readStringFile

fun main() {
    println(readTopCrateLetters9000("src/main/kotlin/day05/test.txt"))
    println(readTopCrateLetters9000("src/main/kotlin/day05/input.txt"))

    println(readTopCrateLetters9001("src/main/kotlin/day05/test.txt"))
    println(readTopCrateLetters9001("src/main/kotlin/day05/input.txt"))

}

fun readTopCrateLetters9000(fileName: String): String {
    val input = readStringFile(fileName)
    val stacks = initStacks(input)
    val movedStacks = performMoves(stacks, input, true)
    return topCharacters(movedStacks)
}

fun readTopCrateLetters9001(fileName: String): String {
    val input = readStringFile(fileName)
    val stacks = initStacks(input)
    val movedStacks = performMoves(stacks, input, false)
    return topCharacters(movedStacks)
}

fun initStacks(input: List<String>): ArrayList<ArrayDeque<Char>> {
    val stacks: ArrayList<ArrayDeque<Char>> = ArrayList()
    for (line in input) {
        if (line.startsWith(" 1 "))
            break
        val crates = line.chunked(4)
        var stackIndex = 1
        for (crate in crates) {
            val content = crate.toCharArray().get(1)
            if (stackIndex > stacks.size) {
                stacks.add(ArrayDeque<Char>())
            }
            if (content != ' ') {
                stacks.get(stackIndex - 1).add(content)
            }
            stackIndex++
        }
    }

    for (stack in stacks) {
        stack.reverse()
    }
    return stacks
}

fun performMoves(stacks: ArrayList<ArrayDeque<Char>>, input: List<String>, oneByOne: Boolean): ArrayList<ArrayDeque<Char>> {
    for (line in input) {
        if (line.startsWith("move")) {
            val move = getMove(line)
            if (move.crates > 0) {
                val movingStack = ArrayDeque<Char>()
                for (i in 1..move.crates) {
                    val crateToMove = stacks[move.from - 1].removeLast()
                    movingStack.add(crateToMove)
                }
                if (!oneByOne)
                    movingStack.reverse()
                stacks[move.to - 1].addAll(movingStack)
            }
        }
    }
    return stacks
}

fun getMove(line: String): Move {
    val moves = Regex("move (\\d*) from (\\d*) to (\\d*)").find(line)
    return moves?.let { Move.fromMatchResult(moves) } ?: Move(0, 0, 0)
}

data class Move(val crates: Int, val from: Int, val to: Int) {
    companion object {
        fun fromMatchResult(match: MatchResult): Move {
            val (crates, from, to) = match.destructured
            return Move(Integer.parseInt(crates), Integer.parseInt(from), Integer.parseInt(to))
        }
    }
}

fun topCharacters(stacks: ArrayList<ArrayDeque<Char>>): String {
    return stacks.map { it.last() }.joinToString("")
}