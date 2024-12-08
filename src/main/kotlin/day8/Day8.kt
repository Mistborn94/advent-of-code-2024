package day8

import helper.Debug
import helper.pairwise
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get
import helper.point.base.points

fun solveA(text: String, debug: Debug = Debug.Disabled) = solve(text.lines(), debug, false)
fun solveB(text: String, debug: Debug = Debug.Disabled): Int = solve(text.lines(), debug, true)

private fun solve(map: List<String>, debug: Debug, b: Boolean): Int {
    val antennaPairs = map.points().filter { map[it] != '.' }
        .groupBy { map[it] }
        .values
        .flatMap { it.pairwise(it) }

    val antinodeLocations =
        antennaPairs.flatMapTo(mutableSetOf()) { (a1, a2) ->
            val d = a2 - a1

            if (b) {
                //For a generalized solution we would need to get the GCD of the delta components
                // and also check between the two points,
                generateSequence(a1) { it - d }.takeWhile { it in map } +
                        generateSequence(a2) { it + d }.takeWhile { it in map }

            } else {
                //The only place where the ratio is correct is exactly d distance away from each of the antennas
                //(It might be possible in between the antennas, but that doesn't occur in our input)
                sequenceOf(a1 - d, a2 + d).filter { it in map }
            }
        }

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
