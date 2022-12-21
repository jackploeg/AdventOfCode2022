package utilities

/**
 * Van https://github.com/peckb1/advent-of-code/tree/main/src/main/kotlin/me/peckb/aoc
 */

/**
 * Similarly to `PathFinder` Node objects, anything used for `Node` here should be a data class
 */
abstract class GenericIntPathFinder<Node : GenericIntPathFinder.NodeWithNeigbours<Node>> :
    PathFinder<Node, Int, GenericIntPathFinder.GenericNodeWithCost<Node>> {
    interface NodeWithNeigbours<Node> {
        fun neighbours(): Map<Node, Int>
    }

    data class GenericNodeWithCost<Node : NodeWithNeigbours<Node>>(val node: Node, val cost: Int) :
        NodeWithCost<Node, Int> {
        override fun neighbours() = node.neighbours().map { GenericNodeWithCost(it.key, it.value) }
        override fun compareTo(other: NodeWithCost<Node, Int>) = cost.compareTo(other.cost())
        override fun node() = node
        override fun cost() = cost
    }

    override fun Int.plus(cost: Int) = this + cost
    override fun maxCost() = Int.MAX_VALUE
    override fun minCost() = 0

    override fun Node.withCost(cost: Int) = GenericNodeWithCost(this, cost)
}
