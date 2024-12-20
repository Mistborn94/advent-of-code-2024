package day16

import helper.Debug
import helper.graph.GraphSearchResult
import helper.graph.shortestPath
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
    return shortestPath(
        start to direction,
        endPredicate = { (p, _) -> p == end },
        neighbours = { (p, d) ->
            buildSet {
                val next = p + d.point
                if (next in map && map[next] != '#') {
                    add(next to d)
                }
                add(p to d.right)
                add(p to d.left)
            }
        },
        cost = { (p1, _), (p2, _) -> if (p1 == p2) 1000 else 1 },
        heuristic = { (p, _) -> (p - end).abs() }
    )
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val searchResult = shortestPath(text)

    return unpackPossiblePaths(searchResult.end(), searchResult.possiblePaths).size
}

fun unpackPossiblePaths(end: Vertex, possiblePrevious: Map<Vertex, Set<Vertex>>): Set<Point> {
    return buildSet {
        add(end.first)
        val prev = possiblePrevious[end] ?: emptySet()
        prev.flatMapTo(this) {
            unpackPossiblePaths(it, possiblePrevious)
        }
    }
}