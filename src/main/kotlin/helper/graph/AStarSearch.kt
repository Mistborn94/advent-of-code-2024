package helper.graph

import java.util.*

typealias NeighbourFunction<T> = (T) -> Iterable<T>
typealias CostFunction<T> = (T, T) -> Int
typealias HeuristicFunction<T> = (T) -> Int

/**
 * Implements A* search to find the shortest path between two vertices
 */
fun <T> findShortestPath(
    start: T,
    end: T,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 },
    heuristic: HeuristicFunction<T> = { 0 }
): GraphSearchResult<T> = findShortestPathByPredicate(start, { it == end }, neighbours, cost, heuristic)

/**
 * Implements A* search to find the shortest path between two vertices using a predicate to determine the ending vertex
 */
fun <T> findShortestPathByPredicate(
    start: T,
    endPredicate: (T) -> Boolean,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 },
    heuristic: HeuristicFunction<T> = { 0 }
): GraphSearchResult<T> {
    val toVisit = PriorityQueue(listOf(ScoredVertex(start, 0, heuristic(start))))
    var endVertex: T? = null

    val possiblePaths = mutableMapOf<T, MutableSet<T>>()
    val seenPoints: MutableMap<T, SeenVertex<T>> = mutableMapOf(start to SeenVertex(0, null))

    while (endVertex == null) {
        if (toVisit.isEmpty()) {
            break
        }

        val (currentVertex, currentCost) = toVisit.remove()
        endVertex = if (endPredicate(currentVertex)) currentVertex else null

        neighbours(currentVertex).forEach { next ->
            val nextCost = currentCost + cost(currentVertex, next)
            val seenVertex = seenPoints[next]
            if (seenVertex != null) {
                val bestCost = seenVertex.cost
                if (nextCost < bestCost) {
                    possiblePaths[next] = mutableSetOf(currentVertex)
                } else if (nextCost == bestCost) {
                    possiblePaths[next]!!.add(currentVertex)
                }
            } else {
                possiblePaths[next] = mutableSetOf(currentVertex)
                toVisit.add(ScoredVertex(next, nextCost, heuristic(next)))
                seenPoints[next] = SeenVertex(nextCost, currentVertex)
            }
        }
    }

    return GraphSearchResult(setOf(start), endVertex, seenPoints, possiblePaths)
}

fun <T> shortestPathToAll(
    start: T,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 }
): GraphSearchResult<T> {
    val initialToVisit = listOf(ScoredVertex(start, 0, 0))
    val seenPoints = shortestPathToAll(initialToVisit, neighbours, cost)
    return GraphSearchResult(setOf(start), null, seenPoints)
}

fun <T> shortestPathToAllFromAny(
    start: Map<T, Int>,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 },
): GraphSearchResult<T> {
    val initialToVisit = start.map { (T, v) -> ScoredVertex(T, v, 0) }
    val seenPoints = shortestPathToAll(initialToVisit, neighbours, cost)
    return GraphSearchResult(start.keys, null, seenPoints)
}

private fun <T> shortestPathToAll(
    initialToVisit: List<ScoredVertex<T>>,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T>
): MutableMap<T, SeenVertex<T>> {
    val toVisit = PriorityQueue(initialToVisit)
    val seenPoints: MutableMap<T, SeenVertex<T>> =
        initialToVisit.associateTo(mutableMapOf()) { it.vertex to SeenVertex(it.score, null) }

    while (toVisit.isNotEmpty()) {
        val (currentVertex, currentScore) = toVisit.remove()

        val nextPoints = neighbours(currentVertex)
            .filter { it !in seenPoints }
            .map { next -> ScoredVertex(next, currentScore + cost(currentVertex, next), 0) }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.associate { it.vertex to SeenVertex(it.score, currentVertex) })
    }
    return seenPoints
}

class GraphSearchResult<T>(
    val start: Set<T>,
    val end: T?,
    val vertices: Map<T, SeenVertex<T>>,
    //Maps vertex to all possible vertices nodes that still results in the shortest path
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
