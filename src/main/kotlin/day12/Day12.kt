package day12

import helper.Debug
import helper.point.Direction
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get
import helper.removeFirst

fun solveA(text: String, debug: Debug = Debug.Disabled): Int = solve(text, debug, false)
fun solveB(text: String, debug: Debug = Debug.Disabled): Int = solve(text, debug, true)

private fun solve(text: String, debug: Debug, b: Boolean): Int {
    val map = text.lines()
    val toVisit = mutableSetOf(Point(0, 0))
    val visited = mutableSetOf<Point>()

    var total = 0
    while (toVisit.isNotEmpty()) {
        val start = toVisit.removeFirst()

        if (start !in visited) {
            val (area, perimeter, next) = expandRegion(map, start)
            visited.addAll(area)
            toVisit.addAll(next)

            total += if (b) {
                area.size * perimeter.count { (p, d) -> p + d.right.point to d !in perimeter }
            } else {
                area.size * perimeter.size
            }
        }
    }
    return total
}

private fun expandRegion(
    map: List<String>,
    start: Point
): Triple<Set<Point>, Set<Pair<Point, Direction>>, Set<Point>> {
    val toVisit = mutableSetOf(start)
    val otherRegions = mutableSetOf<Point>()

    val area = mutableSetOf<Point>()
    val perimeter = mutableSetOf<Pair<Point, Direction>>()

    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst()
        if (area.add(current)) {
            Direction.entries.forEach { direction ->
                val next = current + direction.point
                if (next !in map) {
                    perimeter.add(current to direction)
                } else if (map[next] != map[current]) {
                    perimeter.add(current to direction)
                    otherRegions.add(next)
                } else {
                    toVisit.add(next)
                }
            }
        }
    }
    return Triple(area, perimeter, otherRegions)
}


