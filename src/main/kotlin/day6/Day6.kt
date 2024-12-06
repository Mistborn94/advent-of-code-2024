package day6

import helper.Debug
import helper.point.Direction
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get
import helper.point.base.indexOf

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val grid = text.lines()
    val visited = walk(grid.indexOf('^'), grid)!!

    debug {
        printGrid(grid, visited)
    }

    return visited.size
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val grid = text.lines()
    val start = grid.indexOf('^')
    val possibleObstacles = walk(start, grid)!!
    return possibleObstacles.count {
        it != start && walk(start, grid, it) == null
    }
}

private fun walk(start: Point, grid: List<String>, obstacle: Point? = null): Set<Point>? {
    var position = start
    var direction = Direction.UP
    val visited = mutableSetOf<Pair<Point, Direction>>()

    var loop = false
    while (position in grid && !loop) {
        loop = !visited.add(position to direction)

        val nextStraight = position + direction.point
        if (nextStraight in grid && (grid[nextStraight] == '#' || nextStraight == obstacle)) {
            direction = direction.right
        } else {
            position = nextStraight
        }
    }
    return if (loop) null else visited.mapTo(mutableSetOf()) { it.first }
}

private fun printGrid(grid: List<String>, visited: Set<Point>, possibleObstructions: Set<Point> = emptySet()) {
    println(grid.mapIndexed { y, s ->
        s.mapIndexed { x, _ ->
            val point = Point(x, y)
            when (point) {
                in possibleObstructions -> 'O'
                in visited -> 'X'
                else -> '.'
            }
        }.joinToString(separator = "")
    }.joinToString(separator = "\n"))
}

