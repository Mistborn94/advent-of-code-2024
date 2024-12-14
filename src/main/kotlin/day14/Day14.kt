package day14

import helper.Debug
import helper.point.base.Point
import helper.product

val pattern = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()

fun solveA(text: String, debug: Debug = Debug.Disabled, width: Int = 101, height: Int = 103, time: Int = 100): Int {
    val robots = parseInput(text)

    val middleX = width / 2
    val middleY = height / 2

    val finalPositions = robots.map { (p, v) ->
        moveSingleRobot(p, v, time, width, height)
    }

    debug {
        printRobots(finalPositions, height, width)
    }

    val quadrants = finalPositions.filter { (x, y) -> x != middleX && y != middleY }.groupingBy { (x, y) ->
        if (x < middleX) {
            if (y < middleY) {
                1
            } else {
                2
            }
        } else {
            if (y < middleY) {
                3
            } else {
                4
            }
        }
    }.eachCount()

    return quadrants.values.product()
}

private fun printRobots(
    finalPositions: List<Point>,
    height: Int,
    width: Int
) {
    val eachCount = finalPositions.groupingBy { it }.eachCount()
    repeat(height) { y ->
        repeat(width) { x ->
            if (Point(x, y) in eachCount) {
                print(eachCount[Point(x, y)])
            } else {
                print(".")
            }
        }
        println()
    }
}

private fun robotsUnique(
    robots: List<Pair<Point, Point>>,
    time: Int,
    width: Int,
    height: Int
): Boolean {
    val targets = mutableSetOf<Point>()
    for (robot in robots) {
        if (!targets.add(moveSingleRobot(robot.first, robot.second, time, width, height))) {
            return false
        }
    }
    return true
}

private fun moveSingleRobot(p: Point, v: Point, time: Int, width: Int, height: Int): Point {
    val (x, y) = p + v * time
    return Point(x.mod(width), y.mod(height))
}

private fun parseInput(text: String) = text.lines().map { line ->
    val (a, b, c, d) = pattern.matchEntire(line)!!.destructured

    Point(a.toInt(), b.toInt()) to Point(c.toInt(), d.toInt())
}


fun solveB(text: String, debug: Debug = Debug.Disabled, width: Int = 101, height: Int = 103): Int {
    val robots = parseInput(text)

    //We guess that the robots all need to be in unique positions to draw the picture
    val time = generateSequence(1) { it + 1 }.first { robotsUnique(robots, it, width, height) }
    val finalPositions = robots.map { (p, v) ->
        moveSingleRobot(p, v, time, width, height)
    }
    printRobots(finalPositions, width, height)

    return time
}
