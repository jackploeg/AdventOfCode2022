package day22

import utilities.readStringFile

fun main() {

    println(followPath("src/main/kotlin/day22/test.txt"))
    println(followPath("src/main/kotlin/day22/input.txt"))

}

fun followPath(fileName: String): Int {

    val input = readStringFile(fileName)

    val grid: MutableList<String> = mutableListOf()
    input.takeWhile { it.length > 0 && " .#".contains(it[0]) }.map { grid.add(it) }

    val gridwidth = grid.map { it.length }.max()
    grid.withIndex().forEach { (index, it) -> grid[index] += " ".repeat(gridwidth - it.length) }

    grid.forEach { println(it) }
    var pathDescription = ""
    input.filter { it.length > 0 && "0123456789RL".contains(it[0]) }.first().also { pathDescription = it }

    var x = 0
    while (grid[0][x] == ' ') x++
    var y = 0
    var facing: Facing = Facing.RIGHT
    var instructionPointer = 0

    while (instructionPointer < pathDescription.length) {
        val instruction = readInstruction(pathDescription, instructionPointer)
        instructionPointer += instruction.third
        when (instruction.first) {
            Instruction.TURN_LEFT -> {
                println("Turn Left")
                when (facing) {
                    Facing.RIGHT -> facing = Facing.UP
                    Facing.UP -> facing = Facing.LEFT
                    Facing.LEFT -> facing = Facing.DOWN
                    Facing.DOWN -> facing = Facing.RIGHT
                }
            }

            Instruction.TURN_RIGHT -> {
                println("Turn Right")
                when (facing) {
                    Facing.RIGHT -> facing = Facing.DOWN
                    Facing.DOWN -> facing = Facing.LEFT
                    Facing.LEFT -> facing = Facing.UP
                    Facing.UP -> facing = Facing.RIGHT
                }
            }

            Instruction.MOVE -> {
                println("x = $x, y = $y, facing = $facing")
                println("Move ${instruction.second}")
                when (facing) {
                    Facing.RIGHT -> {
                        run breaking@{
                            (1..instruction.second)
                                .forEach {
                                    if (x < gridwidth - 1 && grid[y][x + 1] == '.')
                                        x++
                                    else if (x == gridwidth - 1 || grid[y][x + 1] == ' ') {
                                        var newX = x - 1
                                        while (newX > -1 && grid[y][newX] != ' ')
                                            newX--
                                        if (grid[y][newX + 1] == '#')
                                            return@breaking
                                        else
                                            x = newX + 1
                                    }
                                }
                        }
                    }

                    Facing.DOWN -> {
                        run breaking@{
                            (1..instruction.second)
                                .forEach {
                                    if (y < grid.size - 1 && grid[y + 1][x] == '.')
                                        y++
                                    else if (y == grid.size - 1 || grid[y + 1][x] == ' ') {
                                        var newY = y - 1
                                        while (newY > -1 && grid[newY][x] != ' ')
                                            newY--
                                        if (grid[newY + 1][x] == '#')
                                            return@breaking
                                        else
                                            y = newY + 1
                                    }
                                }
                        }
                    }

                    Facing.LEFT -> {
                        run breaking@{
                            (1..instruction.second)
                                .forEach {
                                    if (x > 0 && grid[y][x - 1] == '.')
                                        x--
                                    else if (x == 0 || grid[y][x - 1] == ' ') {
                                        var newX = x + 1
                                        while (newX < gridwidth - 1 && grid[y][newX] != ' ')
                                            newX++
                                        if (grid[y][newX - 1] == '#')
                                            return@breaking
                                        else
                                            x = newX - 1
                                    }
                                }
                        }
                    }

                    Facing.UP -> {
                        run breaking@{
                            (1..instruction.second)
                                .forEach {
                                    if (y > 0 && grid[y - 1][x] == '.')
                                        y--
                                    else if (y == 0 || grid[y - 1][x] == ' ') {
                                        var newY = y + 1
                                        while (newY < grid.size - 1 && grid[newY][x] != ' ')
                                            newY++
                                        if (grid[newY - 1][x] == '#')
                                            return@breaking
                                        else
                                            y = newY - 1
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    println("x = $x, y = $y, facing = $facing")
    return (1000 * (y + 1)) + (4 * (x + 1)) + facing.score
}

fun readInstruction(pathDescription: String, pos: Int): Triple<Instruction, Int, Int> {
    if (pathDescription[pos] == 'R')
        return Triple(Instruction.TURN_RIGHT, 0, 1)
    if (pathDescription[pos] == 'L')
        return Triple(Instruction.TURN_LEFT, 0, 1)

    val startpos = pos
    var endpos = pos
    while (endpos < pathDescription.length && "0123456789".contains(pathDescription[endpos]))
        endpos++
    val steps = pathDescription.substring(startpos, endpos).toInt()
    return Triple(Instruction.MOVE, steps, endpos - startpos)
}

enum class Facing(val score: Int) { RIGHT(0), DOWN(1), LEFT(2), UP(3) }

enum class Instruction { MOVE, TURN_LEFT, TURN_RIGHT }