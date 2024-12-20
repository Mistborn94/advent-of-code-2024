package helper.graph

import java.util.*

typealias NeighbourFunction<T> = (T) -> Iterable<T>
typealias CostFunction<T> = (T, T) -> Int
typealias HeuristicFunction<T> = (T) -> Int

/**
 * Implements A* search to find the shortest path between two vertices
 */
fun <T> shortestPath(
    start: T,
    endPredicate: (T) -> Boolean,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 },
    heuristic: HeuristicFunction<T> = { 0 }
): GraphSearchResult<T> =
    shortestPath(listOf(ScoredVertex(start, 0, heuristic(start))), endPredicate, neighbours, cost, heuristic)

/**
 * Implements Dijkstra's algorithm to get the shortest path from a starting vertex to all reachable vertices
 */
fun <T> shortestPathToAll(
    start: T,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 }
): GraphSearchResult<T> = shortestPath(listOf(ScoredVertex(start, 0, 0)), { false }, neighbours, cost)

fun <T> shortestPathToAllFromMultiple(
    start: Map<T, Int>,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 },
): GraphSearchResult<T> =
    shortestPath(start.map { (k, v) -> ScoredVertex(k, v, 0) }, { false }, neighbours, cost)

/**
 * Actual shortest path implementation
 */
private fun <T> shortestPath(
    initialToVisit: List<ScoredVertex<T>>,
    endPredicate: (T) -> Boolean,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T>,
    heuristic: HeuristicFunction<T> = { 0 },
): GraphSearchResult<T> {
    val toVisit = PriorityQueue(initialToVisit)
    val seenPoints: MutableMap<T, SeenVertex<T>> =
        initialToVisit.associateTo(mutableMapOf()) { (vertex, score) -> vertex to SeenVertex(score, null) }
    val possiblePaths = mutableMapOf<T, MutableSet<T>>()

    var endVertex: T? = null
    while (endVertex == null && toVisit.isNotEmpty()) {
        val (currentVertex, currentCost) = toVisit.remove()
        endVertex = if (endPredicate(currentVertex)) currentVertex else null

        neighbours(currentVertex).forEach { next ->
            val nextCost = currentCost + cost(currentVertex, next)
            val heuristicCost = heuristic(next)
            val bestCost = seenPoints[next]?.cost ?: Int.MAX_VALUE

            if (nextCost < bestCost) {
                possiblePaths[next] = mutableSetOf(currentVertex)
                seenPoints[next] = SeenVertex(nextCost, currentVertex)
                toVisit.add(ScoredVertex(next, nextCost, heuristicCost))
            } else if (nextCost == bestCost) {
                possiblePaths[next]!!.add(currentVertex)
            }
        }
    }
    val starts = initialToVisit.mapTo(mutableSetOf()) { it.vertex }
    return GraphSearchResult(starts, endVertex, seenPoints, possiblePaths)
}

class GraphSearchResult<T>(
    val start: Set<T>,
    val end: T?,
    //Maps vertices to costs and previous vertices
    val vertices: Map<T, SeenVertex<T>>,
    //Maps vertex to all possible previous vertices that still results in a shortest path
    val possiblePaths: Map<T, Set<T>> = emptyMap()
) {

    fun getScore(vertex: T): Int =
        vertices[vertex]?.cost ?: throw IllegalStateException("Result for $vertex not available")

    fun getScore(): Int = end?.let { getScore(it) } ?: throw IllegalStateException("No path found")
    fun getScoreOrNull(): Int? = end?.let { getScore(it) }

    fun getPath() = end?.let { getPath(it, emptyList()) } ?: throw IllegalStateException("No path found")
    fun getPath(end: T) = getPath(end, emptyList())
    fun getVertexInPath(end: T, startCondition: (T) -> Boolean) =
        getPathItem(end, startCondition) ?: throw IllegalStateException("No path found")

    fun seen(): Set<T> = vertices.keys
    fun end(): T = end ?: throw IllegalStateException("No path found")

    private tailrec fun getPath(endVertex: T, pathEnd: List<T>): List<T> {
        val previous = vertices[endVertex]?.prev

        return if (previous == null) {
            listOf(endVertex) + pathEnd
        } else {
            getPath(previous, listOf(endVertex) + pathEnd)
        }
    }

    tailrec fun getStart(endVertex: T): T {
        val previous = vertices[endVertex]?.prev

        return if (previous == null) {
            endVertex
        } else {
            getStart(previous)
        }
    }

    private tailrec fun getPathItem(endVertex: T, startCondition: (T) -> Boolean = { it == null }): T? {
        val previous = vertices[endVertex]?.prev

        return if (previous == null) {
            null
        } else if (startCondition(previous)) {
            previous
        } else {
            getPathItem(previous, startCondition)
        }
    }
}

data class SeenVertex<T>(val cost: Int, val prev: T?)

data class ScoredVertex<T>(val vertex: T, val score: Int, val heuristic: Int) : Comparable<ScoredVertex<T>> {
    override fun compareTo(other: ScoredVertex<T>): Int = (score + heuristic).compareTo(other.score + other.heuristic)
}
