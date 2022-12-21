package day16

import utilities.GenericIntPathFinder
import utilities.readStringFile

private const val TIME_TO_OPEN_VALVE = 1

fun main() {
    partOne("src/main/kotlin/day16/test.txt")
    partOne("src/main/kotlin/day16/input.txt")

    partTwo("src/main/kotlin/day16/test.txt")
    partTwo("src/main/kotlin/day16/input.txt")
}


fun partOne(fileName: String) {
    val input = readStringFile(fileName)
    val (valveMap, paths) = preProcess(input.map { valves(it) })
    println(determineMaxFlow("AA", 0, 30, valveMap, paths, emptySet()))
}

fun partTwo(fileName: String) {
    val input = readStringFile(fileName)
    val (valveMap, paths) = preProcess(input.map { valves(it) })
    println(determineMaxFlow2("AA", "AA", 0, 0, 26, valveMap, paths, emptySet()))
}

private fun preProcess(input: List<Valve>): Pair<Map<String, Valve>, Map<Valve, Map<ValveNodeWithNeigbours, Int>>> {
    val valveMap = mutableMapOf<String, Valve>().apply {
        input.forEach { this[it.id] = it }
    }

    val solver = ValvePathFinder()
    val paths = valveMap.values.associateWith {
        solver.solve(ValveNodeWithNeigbours(it.id).usingValves(valveMap))
    }

    return valveMap.filter { it.key == "AA" || it.value.flowRate > 0 } to paths
}

private fun determineMaxFlow(
    currentLocation: String,
    timeAtLocation: Int,
    totalTimeAllowed: Int,
    valveMap: Map<String, Valve>,
    paths: Map<Valve, Map<ValveNodeWithNeigbours, Int>>,
    previouslyOpenedValues: Set<String>
): Int {
    val valveOptions = paths[valveMap[currentLocation]!!]!!.filter { (vn, _) ->
        (valveMap[vn.valveId]?.flowRate ?: 0) > 0 && !previouslyOpenedValues.contains(vn.valveId)
    }

    return valveOptions.maxOfOrNull { (valveToOpen, costToTravelToNode) ->
        val timeAtLocationAfterOpening = timeAtLocation + costToTravelToNode + TIME_TO_OPEN_VALVE
        if (timeAtLocationAfterOpening >= totalTimeAllowed) {
            0
        } else {
            val valve = valveMap[valveToOpen.valveId]!!
            val minutesOpen = totalTimeAllowed - timeAtLocationAfterOpening
            val totalPressureGained = minutesOpen * valve.flowRate

            totalPressureGained + determineMaxFlow(
                currentLocation = valveToOpen.valveId,
                timeAtLocation = timeAtLocationAfterOpening,
                totalTimeAllowed = totalTimeAllowed,
                valveMap = valveMap,
                paths = paths,
                previouslyOpenedValues = previouslyOpenedValues.plus(valveToOpen.valveId)
            )
        }
    } ?: 0 // in case we have no valve options we'd have a null max
}

private fun determineMaxFlow2(
    myLocation: String,
    elephantLocation: String,
    myTimeAfterOpeningMyValve: Int,
    elephantTimeAfterOpeningTheirValve: Int,
    totalTimeAllowed: Int,
    valveMap: Map<String, Valve>,
    paths: Map<Valve, Map<ValveNodeWithNeigbours, Int>>,
    previouslyOpenedValues: Set<String>
): Int {
    val myOptions = paths[valveMap[myLocation]!!]!!.filter { (vn, _) ->
        ((valveMap[vn.valveId]?.flowRate) ?: 0) > 0 && !previouslyOpenedValues.contains(vn.valveId)
    }
    val elephantOptions = paths[valveMap[elephantLocation]!!]!!.filter { (vn, _) ->
        ((valveMap[vn.valveId]?.flowRate) ?: 0) > 0 && !previouslyOpenedValues.contains(vn.valveId)
    }

    return myOptions.maxOfOrNull { (myValve, myTravelCost) ->
        val myTimeAtLocationAfterOpening = myTimeAfterOpeningMyValve + myTravelCost + TIME_TO_OPEN_VALVE
        if (myTimeAtLocationAfterOpening < totalTimeAllowed) {
            val myValveNode = valveMap[myValve.valveId]!!
            val myMinutesToOpen = totalTimeAllowed - myTimeAtLocationAfterOpening
            val myTotalPressureGained = myMinutesToOpen * myValveNode.flowRate

            myTotalPressureGained + (elephantOptions.maxOfOrNull { (elephantValve, elephantTravelCost) ->
                if (myValve == elephantValve) {
                    -1
                } else {
                    val elephantTimeAtLocationAfterOpening =
                        elephantTimeAfterOpeningTheirValve + elephantTravelCost + TIME_TO_OPEN_VALVE
                    if (elephantTimeAtLocationAfterOpening < totalTimeAllowed) {
                        val elephantValveNode = valveMap[elephantValve.valveId]!!
                        val elephantMinutesToOpen = totalTimeAllowed - elephantTimeAtLocationAfterOpening
                        val elephantTotalPressureGained = elephantMinutesToOpen * elephantValveNode.flowRate

                        elephantTotalPressureGained + determineMaxFlow2(
                            myLocation = myValve.valveId,
                            elephantLocation = elephantValve.valveId,
                            myTimeAfterOpeningMyValve = myTimeAtLocationAfterOpening,
                            elephantTimeAfterOpeningTheirValve = elephantTimeAtLocationAfterOpening,
                            totalTimeAllowed = totalTimeAllowed,
                            valveMap = valveMap,
                            paths = paths,
                            previouslyOpenedValues = previouslyOpenedValues.plus(myValve.valveId)
                                .plus(elephantValve.valveId),
                        )
                    } else {
                        0
                    } // going to and opening would take too long
                }
            } ?: 0)
        } else {
            0
        } // going to and opening would take too long
    } ?: 0
}

private fun valves(line: String): Valve {
    // Valve AA has flow rate=0; valves lead to valves DD, II, BB
    val parts = line.split(" ")
    val id = parts[1]
    val flowRate = parts[4].split("=")[1].dropLast(1).toInt()
    val neighboringValveIds = parts.drop(9).map { it.take(2) }

    return Valve(id, flowRate, neighboringValveIds)
}

data class Valve(val id: String, val flowRate: Int, val neighboringValveIds: List<String>)

data class ValveNodeWithNeigbours(val valveId: String) : GenericIntPathFinder.NodeWithNeigbours<ValveNodeWithNeigbours> {
    lateinit var valveMap: Map<String, Valve>

    fun usingValves(valveMap: Map<String, Valve>) = apply { this.valveMap = valveMap }

    override fun neighbours(): Map<ValveNodeWithNeigbours, Int> {
        return valveMap[valveId]!!
            .neighboringValveIds
            .map { ValveNodeWithNeigbours(it).usingValves(valveMap) }
            .associateWith { 1 }
    }
}

class ValvePathFinder : GenericIntPathFinder<ValveNodeWithNeigbours>()


