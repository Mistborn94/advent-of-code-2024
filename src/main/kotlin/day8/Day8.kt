package day8

import helper.Debug
import helper.pairwise
import helper.point.base.Point
import helper.point.base.get
import helper.point.base.points
import kotlin.math.max
import kotlin.math.min

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines()
    val antennaLocations = map.points().filter { map[it] != '.' }

    val antennaPairs = antennaLocations
        .groupBy { map[it] }
        .values
        .flatMap { it.pairwise(it).filter { (a, b) -> a != b } }

    val antinodeLocations = antinodeLocations(map, antennaPairs, debug)

    debug {
        println(resultMap(map, antinodeLocations))
    }

    return antinodeLocations.size
}

fun antinodeLocations(
    map: List<String>,
    antennaPairs: List<Pair<Point, Point>>,
    debug: Debug,
    b: Boolean = false
): Set<Point> {
    return map.points().filterTo(mutableSetOf()) { point ->
        antennaPairs.any { (a1, a2) ->
            val d1 = point.euclideanDistance(a1)
            val slope1 = point.slopeTo(a1)
            val d2 = point.euclideanDistance(a2)
            val slope2 = point.slopeTo(a2)

            val found = slope1 == slope2 && (b || max(d1, d2) == (min(d1, d2) * 2))

            if (found) {
                debug {
                    println("Found point: $point between $a1, $a2. Slope: $slope1, Distances: $d1, $d2")
                }
            }

            found
        }
    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines()
    val antennaLocations = map.points().filter { map[it] != '.' }

    val antennaPairs = antennaLocations
        .groupBy { map[it] }
        .values
        .flatMap { it.pairwise(it) }

    val antinodeLocations = antinodeLocations(map, antennaPairs, debug, true) + antennaLocations

    debug {
        println(resultMap(map, antinodeLocations))
    }

    return antinodeLocations.size
}

private fun resultMap(
    map: List<String>,
    antinodeLocations: Set<Point>
) = map.mapIndexed { y, line ->
    line.mapIndexed { x, c ->
        val point = Point(x, y)
        if (point in antinodeLocations && map[point] == '.') '#' else if (point in antinodeLocations) '*' else c
    }.joinToString("")
}.joinToString(separator = "\n")
