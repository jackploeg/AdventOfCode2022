import utilities.readStringFile
import java.util.LinkedList
import java.util.Queue

/* Thanks to https://raw.githubusercontent.com/ArpitShukIa/AdventOfCode2022/main/src/Day19.kt */

fun main() {

    println(mineMinerals("src/main/kotlin/day19/test.txt"))
    println(mineMinerals("src/main/kotlin/day19/input.txt"))

    println(mineFirst3Blueprints("src/main/kotlin/day19/test.txt"))
    println(mineFirst3Blueprints("src/main/kotlin/day19/input.txt"))
}

fun mineMinerals(fileName: String): Int {

    val input = readStringFile(fileName)

    return input.sumOf { line ->
        val tokens = Regex("\\d+").findAll(line).map { it.value.toInt() }.toList()
        tokens[0] * solve(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], 24)
    }
}

fun mineFirst3Blueprints(fileName: String): Int {
    val input = readStringFile(fileName)

    return input.take(3).map { line ->
        val tokens = Regex("\\d+").findAll(line).map { it.value.toInt() }.toList()
        solve(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], 32)
    }.reduce(Int::times)
}

data class State(
    val ore: Int, val clay: Int, val obsidian: Int, val geodes: Int,
    val oreRobots: Int, val clayRobots: Int, val obsidianRobots: Int, val geodeRobots: Int, val time: Int
)

fun solve(
    oreRobotCostOre: Int,
    clayRobotCostOre: Int,
    obsidianRobotCostOre: Int,
    obsidianRobotCostClay: Int,
    geodeRobotCostOre: Int,
    geodeRobotCostObsidian: Int,
    time: Int
): Int {
    var maxGeodes = 0
    val state = State(0, 0, 0, 0, 1, 0, 0, 0, time)
    val queue: Queue<State> = LinkedList()
    queue.add(state)
    val seen = mutableSetOf<State>()
    while (queue.isNotEmpty()) {
        var currentState = queue.poll()
        var (ore, clay, obsidian, geodes, oreRobots, clayRobots, obisidanRobots, geodeRobots, timeLeft) = currentState
        maxGeodes = maxOf(maxGeodes, geodes)
        if (timeLeft == 0)
            continue
        val maxOreRobotsNeeded = maxOf(oreRobotCostOre, clayRobotCostOre, obsidianRobotCostOre, geodeRobotCostOre)
        // Discard extra resources
        oreRobots = minOf(oreRobots, maxOreRobotsNeeded)
        clayRobots = minOf(clayRobots, obsidianRobotCostClay)
        obisidanRobots = minOf(obisidanRobots, geodeRobotCostObsidian)
        ore = minOf(ore, maxOreRobotsNeeded * timeLeft - oreRobots * (timeLeft - 1))
        clay = minOf(clay, obsidianRobotCostClay * timeLeft - clayRobots * (timeLeft - 1))
        obsidian = minOf(obsidian, geodeRobotCostObsidian * timeLeft - obisidanRobots * (timeLeft - 1))

        currentState = State(ore, clay, obsidian, geodes, oreRobots, clayRobots, obisidanRobots, geodeRobots, timeLeft)
        if (currentState in seen)
            continue
        seen += currentState

        // What if we do nothing
        queue.add(
            State(
                ore + oreRobots,
                clay + clayRobots,
                obsidian + obisidanRobots,
                geodes + geodeRobots,
                oreRobots,
                clayRobots,
                obisidanRobots,
                geodeRobots,
                timeLeft - 1
            )
        )
        // what if we order an Ore robot
        if (ore >= oreRobotCostOre)
            queue.add(
                State(
                    ore - oreRobotCostOre + oreRobots,
                    clay + clayRobots,
                    obsidian + obisidanRobots,
                    geodes + geodeRobots,
                    oreRobots + 1,
                    clayRobots,
                    obisidanRobots,
                    geodeRobots,
                    timeLeft - 1
                )
            )
        // what if we order a Clay robot
        if (ore >= clayRobotCostOre)
            queue.add(
                State(
                    ore - clayRobotCostOre + oreRobots,
                    clay + clayRobots,
                    obsidian + obisidanRobots,
                    geodes + geodeRobots,
                    oreRobots,
                    clayRobots + 1,
                    obisidanRobots,
                    geodeRobots,
                    timeLeft - 1
                )
            )
        // what if we order an Obsidian robot
        if (ore >= obsidianRobotCostOre && clay >= obsidianRobotCostClay)
            queue.add(
                State(
                    ore - obsidianRobotCostOre + oreRobots,
                    clay - obsidianRobotCostClay + clayRobots,
                    obsidian + obisidanRobots,
                    geodes + geodeRobots,
                    oreRobots,
                    clayRobots,
                    obisidanRobots + 1,
                    geodeRobots,
                    timeLeft - 1
                )
            )
        // what if we order a geode cracking robot
        if (ore >= geodeRobotCostOre && obsidian >= geodeRobotCostObsidian)
            queue.add(
                State(
                    ore - geodeRobotCostOre + oreRobots,
                    clay + clayRobots,
                    obsidian - geodeRobotCostObsidian + obisidanRobots,
                    geodes + geodeRobots,
                    oreRobots,
                    clayRobots,
                    obisidanRobots,
                    geodeRobots + 1,
                    timeLeft - 1
                )
            )
    }
    return maxGeodes
}


