package day16

import helper.Debug
import helper.graph.GraphSearchResult
import helper.graph.findShortestPathByPredicate
import helper.point.Direction
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get
import helper.point.base.indexOf

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val graphSearchResult = shortestPath(text)
    return graphSearchResult.getScore()
}

typealias Vertex = Pair<Point, Direction>

private fun shortestPath(text: String): GraphSearchResult<Vertex> {
    val map = text.lines()
    val start = map.indexOf('S')
    val end = map.indexOf('E')
    val direction = Direction.EAST
    val graphSearchResult = findShortestPathByPredicate(
        start to direction,
        { (p, _) -> p == end },
        { (p, d) ->
            buildSet {
                val next = p + d.point
                if (next in map && map[next] != '#') {
                    add(next to d)
                }
                add(p to d.right)
                add(p to d.left)
            }
        },
        { (p1, d1), (p2, d2) ->
            if (p1 == p2) 1000 else 1
        }
    )
    return graphSearchResult
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines()
    val start = map.indexOf('S')
    val end = map.indexOf('E')
    val graphSearchResult = shortestPath(text)
    val maxCost = graphSearchResult.getScore()
    val cache = graphSearchResult.vertices.mapValuesTo(mutableMapOf()) { maxCost - it.value.cost }
    return graphDfs(map, start to Direction.EAST, end, emptySet(), maxCost, cache).size
}

fun graphDfs(
    map: List<String>,
    current: Vertex,
    end: Point,
    path: Set<Vertex>,
    remainingBudget: Int,
    cache: MutableMap<Vertex, Int>
): Set<Point> {
    val seen = cache[current]
    return if (remainingBudget == 0) {
        if (current.first == end) {
            path.mapTo(mutableSetOf(current.first)) { p -> p.first }
        } else {
            emptySet()
        }
    } else if (remainingBudget < 0 || seen != null && remainingBudget < seen) {
        emptySet()
    } else {
        cache[current] = remainingBudget
        val neighbours = buildSet {
            val next = current.first + current.second.point
            if (next in map && map[next] != '#') {
                add((next to current.second) to 1)
            }
            add((current.first to current.second.right) to 1000)
            add((current.first to current.second.left) to 1000)
        }.filter { it.first !in path }

        neighbours.flatMapTo(mutableSetOf()) { (v, s) ->
            graphDfs(map, v, end, path + current, remainingBudget - s, cache)
        }
    }
}
