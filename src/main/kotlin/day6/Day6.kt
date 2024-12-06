package day6

import helper.Debug
import helper.point.Direction
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get
import helper.point.base.indexOf

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val grid = text.lines()
    val visited = getVisited(grid.indexOf('^'), grid)

    debug {
        printGrid(grid, visited)
    }

    return visited.size
}

private fun getVisited(start: Point, grid: List<String>): Set<Point> {
    var position = start
    var direction = Direction.UP
    val visited = mutableSetOf<Point>()

    while (position in grid) {
        visited.add(position)

        val nextStraight = position + direction.point
        if (nextStraight !in grid || grid[nextStraight] != '#') {
            position = nextStraight
        } else {
            direction = direction.right
        }
    }
    return visited
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


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val grid = text.lines()
    val start = grid.indexOf('^')
    val possibleObstacles = getVisited(start, grid)
    return possibleObstacles.count {
        it != start && loopIfObstacle(grid, start, it)
    }
}

fun loopIfObstacle(grid: List<String>, start: Point, obstacle: Point): Boolean {
    var pos = start
    var direction = Direction.UP
    var loop = false
    val visited = mutableSetOf<Pair<Point, Direction>>()
    while (pos in grid && !loop) {
        loop = !visited.add(pos to direction)

        val nextStraight = pos + direction.point

        if (nextStraight in grid && (grid[nextStraight] == '#' || nextStraight == obstacle)) {
            direction = direction.right
        } else {
            pos = nextStraight
        }
    }

    return loop
}
