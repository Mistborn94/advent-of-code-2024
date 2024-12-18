package day18

import helper.Debug
import helper.graph.findShortestPathByPredicate
import helper.point.base.Point

fun solveA(text: String, debug: Debug = Debug.Disabled, endIndex: Int = 70, simDuration: Int = 1024): Int {
    val start = Point(0, 0)
    val end = Point(endIndex, endIndex)
    val coordinateRange = 0..endIndex
    val corrupted =
        parseInput(text).take(simDuration).toSet()
    return walk(start, end, corrupted, coordinateRange).getScore()
}

private fun walk(
    start: Point,
    end: Point,
    corrupted: Set<Point>,
    coordinateRange: IntRange
) = findShortestPathByPredicate(
    start,
    { it == end },
    { it.neighbours().filter { n -> n !in corrupted && n.x in coordinateRange && n.y in coordinateRange } },
    heuristic = { it.manhattanDistance(end) }
)

private fun parseInput(text: String): List<Point> {
    val corrupted =
        text.lines().map { line -> line.split(",").map { it.toInt() }.let { (x, y) -> Point(x, y) } }
    return corrupted
}


fun solveB(text: String, debug: Debug = Debug.Disabled, endIndex: Int = 70): String {
    val start = Point(0, 0)
    val end = Point(endIndex, endIndex)
    val coordinateRange = 0..endIndex
    val corrupted = parseInput(text)

    var i = 0
    while (i < corrupted.size) {
        val currentCorrupted = corrupted.take(i + 1).toSet()
        val result = walk(start, end, currentCorrupted, coordinateRange)

        if (result.end == null) {
            break;
        }
        i++
    }

    return corrupted[i].let { "${it.x},${it.y}" }
}
