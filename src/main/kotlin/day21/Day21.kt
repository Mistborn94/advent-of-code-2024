package day21

import helper.Debug
import helper.cartesianProduct
import helper.point.base.Point
import helper.point.base.get
import helper.point.base.indexOf
import kotlin.math.absoluteValue

val numericKeypad = Keypad("789456123 0A")
val directionalKeypad = Keypad(" ^A<v>")

fun solveA(text: String, debug: Debug = Debug.Disabled): Long {
    val codes = text.lines()
    return solve(debug, codes, 2)
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val codes = text.lines()
    return solve(debug, codes, 25)
}

private fun solve(debug: Debug, codes: List<String>, directionalRobotCount: Int): Long {
    return codes.sumOf { code ->
        val numericPart = code.dropLast(1).toInt()
        val solution = "A$code".zipWithNext().sumOf { pair ->
            numericKeypad.paths(pair, 1).minOf { it.score(directionalRobotCount) }
        }
        solution * numericPart
    }
}

typealias ButtonPress = Pair<Char, Int>

val segmentExpansionCache = mutableMapOf<PathSegment, List<List<PathSegment>>>()

data class PathSegment(val path: List<ButtonPress>) {
    private val scores = mutableMapOf<Int, Long>()

    init {
        require(path.last().first == 'A') { "Last step in segment must be A" }
    }

    private fun expand(): List<List<PathSegment>> = segmentExpansionCache.getOrPut(this) {
        val starts = directionalKeypad.paths('A' to path.first().first, path.first().second).map { listOf(it) }

        path.zipWithNext().fold(starts) { acc, (first, second) ->
            val movement = directionalKeypad.paths(first.first to second.first, second.second)
            acc.cartesianProduct(movement) { a, b -> a + b }
        }
    }

    fun score(remainingRobots: Int): Long {
        return scores.getOrPut(remainingRobots) {
            if (remainingRobots == 0) {
                path.sumOf { (_, d) -> d.toLong() }
            } else {
                expand().minOf { option -> totalScore(option, remainingRobots - 1) }
            }
        }
    }

    private fun totalScore(pathSegments: List<PathSegment>, remainingRobots: Int) =
        pathSegments.sumOf { it.score(remainingRobots) }

    override fun toString() = path.joinToString(separator = "") { (first, second) -> "$first".repeat(second) }
}

class Keypad(text: String) {
    private val grid = text.chunked(3)
    private val chars = text.toList().filter { it != ' ' }

    private val paths = chars.cartesianProduct(chars) { startName, endName ->
        val entryKey = startName to endName
        entryKey to possiblePaths(grid.indexOf(endName), grid.indexOf(startName))
    }.toMap()

    fun paths(pair: Pair<Char, Char>, pushCount: Int): List<PathSegment> {
        val pushCommand = 'A' to pushCount
        return paths.getValue(pair).map { PathSegment(it + pushCommand) }
    }

    private fun possiblePaths(
        endPosition: Point,
        startPosition: Point
    ): Set<List<ButtonPress>> {
        val hDirection = if (endPosition.x > startPosition.x) '>' else '<'
        val vDirection = if (endPosition.y > startPosition.y) 'v' else '^'

        val verticalCommand = vDirection to (endPosition.y - startPosition.y).absoluteValue
        val horizontalCommand = hDirection to (endPosition.x - startPosition.x).absoluteValue

        return buildSet {
            if (grid[Point(startPosition.x, endPosition.y)] != ' ') {
                add(listOf(verticalCommand, horizontalCommand).filter { (_, d) -> d > 0 })
            }
            if (grid[Point(endPosition.x, startPosition.y)] != ' ') {
                add(listOf(horizontalCommand, verticalCommand).filter { (_, d) -> d > 0 })
            }
        }
    }

}
