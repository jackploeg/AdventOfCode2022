package utilities
import java.util.PriorityQueue

/**
 * Van https://github.com/peckb1/advent-of-code/tree/main/src/main/kotlin/me/peckb/aoc
 */

/**
 * NOTE: whatever class you use for `Node` should be a `data class`
 */
interface PathFinder<Node, Cost : Comparable<Cost>, NodeWithCost: utilities.NodeWithCost<Node, Cost>> {
    fun solve(start: Node, end: Node? = null, comparator: Comparator<Node>? = null): MutableMap<Node, Cost> {
        val toVisit = PriorityQueue<NodeWithCost>().apply { add(start.withCost(minCost())) }
        val visited = mutableSetOf<Node>()
        val currentCosts = mutableMapOf<Node, Cost>()
            .withDefault { maxCost() }
            .apply { this[start] = minCost() }

        while (toVisit.isNotEmpty()) {
            val current: NodeWithCost = toVisit.poll().also { visited.add(it.node()) }

            val foundEnd: Boolean? = end?.let { node: Node ->
                comparator?.let { it.compare(current.node(), end) == 0 } ?: (current.node() == node)
            }

            if (foundEnd == true) return currentCosts

            current.neighbours().forEach { neighbor ->
                if (!visited.contains(neighbor.node())) {
                    val newCost = current.cost() + neighbor.cost()
                    if (newCost < currentCosts.getValue(neighbor.node())) {
                        currentCosts[neighbor.node()] = newCost
                        toVisit.add(neighbor.node().withCost(newCost))
                    }
                }
            }
        }

        return currentCosts
    }

    infix operator fun Cost.plus(cost: Cost): Cost
    fun Node.withCost(cost: Cost): NodeWithCost
    fun minCost(): Cost
    fun maxCost(): Cost
}

interface NodeWithCost<Node, Cost> : Comparable<NodeWithCost<Node, Cost>> {
    fun neighbours(): List<NodeWithCost<Node, Cost>>
    fun node(): Node
    fun cost(): Cost
}
