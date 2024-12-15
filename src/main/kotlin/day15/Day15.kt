package day15

import helper.Debug
import helper.point.Direction
import helper.point.base.Point
import helper.point.base.get
import helper.point.base.indexOf
import helper.point.base.points
import helper.removeFirst
import kotlin.collections.component1
import kotlin.collections.component2

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val (map, instructions) = text.split("\n\n").let { (first, second) ->
        first.lines() to second.replace("\n", "")
    }

    val walls = mutableSetOf<Point>()
    val boxes = mutableSetOf<Point>()
    var robot = map.indexOf('@')

    map.points().forEach { p ->
        val c = map[p]
        when (c) {
            '#' -> walls += p
            'O' -> boxes += p
        }
    }

    instructions.forEach { c ->
        val direction = Direction.parse(c)
        val boxesToMove = mutableSetOf<Point>()
        var nextPoint = robot + direction.point
        while (nextPoint in boxes) {
            boxesToMove.add(nextPoint)
            nextPoint += direction.point
        }
        if (nextPoint !in walls) {
            boxes.removeAll(boxesToMove)
            boxes.addAll(boxesToMove.map { it + direction.point })
            robot += direction.point
        }
    }

    return boxes.sumOf { (x, y) -> x + y * 100 }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val (map, instructions) = text.split("\n\n").let { (first, second) ->
        first.lines() to second.replace("\n", "")
    }

    val width = map[0].length * 2
    val height = map.size
    val walls = mutableSetOf<Point>()
    val boxes = mutableSetOf<Pair<Point, Point>>()

    var robot = map.indexOf('@').let { (x, y) -> Point(x * 2, y) }

    map.points().forEach { p ->
        val p1 = Point(p.x * 2, p.y)
        val p2 = Point(p.x * 2 + 1, p.y)
        val c = map[p]
        when (c) {
            '#' -> {
                walls += p1
                walls += p2
            }

            'O' -> {
                boxes.add(p1 to p2)
            }
        }
    }

    debug {
        println("Initial State:")
        printMap(height, width, robot, walls, boxes)
    }

    instructions.forEachIndexed { i, c ->
        val direction = Direction.parse(c)

        val boxesToMove = mutableSetOf<Pair<Point, Point>>()
        val boxPointsToVisit = mutableSetOf(robot + direction.point)
        while (boxPointsToVisit.isNotEmpty() && boxPointsToVisit.first() !in walls) {
            val nextBoxPoint = boxPointsToVisit.removeFirst()
            //For left and right only one of these is possible.
            //But for the sake of simplicity, we just search for both
            val possibleBoxes = setOf(
                Pair(nextBoxPoint + Direction.LEFT.point, nextBoxPoint),
                Pair(nextBoxPoint, nextBoxPoint + Direction.RIGHT.point)
            )
            val box = possibleBoxes.firstOrNull { it in boxes }
            if (box != null) {
                boxesToMove.add(box)
                when (direction) {
                    Direction.UP, Direction.DOWN -> {
                        boxPointsToVisit.add(box.first + direction.point)
                        boxPointsToVisit.add(box.second + direction.point)
                    }

                    Direction.LEFT -> boxPointsToVisit.add(box.first + direction.point)
                    else -> boxPointsToVisit.add(box.second + direction.point)
                }
            }
        }
        if (boxPointsToVisit.none { it in walls }) {
            boxes.removeAll(boxesToMove)
            boxes.addAll(boxesToMove.map { (first, second) -> first + direction.point to second + direction.point })
            robot += direction.point
        }
        debug {
            println("Movement $c ($i):")
            printMap(height, width, robot, walls, boxes)
        }
    }

    return boxes.map { it.first }.sumOf { (x, y) -> x + y * 100 }
}

private fun printMap(
    height: Int,
    width: Int,
    robot: Point,
    walls: Set<Point>,
    boxes: Set<Pair<Point, Point>>
) {
    repeat(height) { y ->
        repeat(width) { x ->
            val point = Point(x, y)
            val boxL = Pair(point, point + Direction.RIGHT.point)
            val boxR = Pair(point + Direction.LEFT.point, point)
            when {
                point == robot -> print('@')
                point in walls -> print('#')
                boxL in boxes -> print('[')
                boxR in boxes -> print(']')
                else -> print('.')
            }
        }
        println()
    }
    println()
}
