package day10

import utitlities.readStringFile

fun main() {
    println(runProgram("src/main/kotlin/day10/test.txt"))
    println(runProgram("src/main/kotlin/day10/input.txt"))
}

fun runProgram(fileName: String): Long {
    val input = readStringFile(fileName)
    val reporter = Reporter()
    val runner = Runner(reporter)
    input.forEach { processCommand(it, runner) }

    return reporter.sumOfX
}

fun processCommand(commandline: String, runner: Runner) {
    val instructions = commandline.split(" ")
    val command = instructions.get(0)
    when (command) {
        "noop" -> runner.tick()
        "addx" -> runner.addx(Integer.parseInt(instructions.get(1)))
    }
}

class Runner(val reporter: Reporter) {
    var tick = 0
    var x = 1

    fun tick(ticks: Int = 1) {
        tick += ticks
        reporter.report(tick, x)
    }

    fun addx(increment: Int) {
        for (i in 1..2) {
            reporter.report(++tick, x)
        }
        x += increment
    }
}

class Reporter() {
    val reportTicks: ArrayList<Int> = arrayListOf(20, 60, 100, 140, 180, 220)
    var sumOfX = 0L

    fun report(tick: Int, x: Int) {
        if ((tick - 1) % 40 in ( x - 1 .. x + 1)) {
            print("#")
        }
        else {
            print(".")
        }
        if (tick%40==0)
            println()
        if (tick in reportTicks) {
            sumOfX += x * tick
        }
    }
}
