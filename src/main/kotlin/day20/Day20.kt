package day20

import helper.Debug
import helper.graph.shortestPathToAll
import helper.point.base.contains
import helper.point.base.get
import helper.point.base.indexOf

fun solveA(text: String, debug: Debug = Debug.Disabled, timeSavedGoal: Int = 100): Int = solve(text, timeSavedGoal, 2)

fun solveB(text: String, debug: Debug = Debug.Disabled, timeSavedGoal: Int = 100): Int = solve(text, timeSavedGoal, 20)

private fun solve(text: String, timeSavedGoal: Int, cheatLength: Int): Int {
    val map = text.lines()
    val start = map.indexOf('S')
    val end = map.indexOf('E')

    val shortestPath =
        shortestPathToAll(start, neighbours = { it.neighbours().filter { n -> n in map && map[n] != '#' } })
    val path = shortestPath.getPath(end)

    return path.withIndex().sumOf { (index, cheatStart) ->
        path.drop(index + 1).count { cheatEnd ->
            val distance = (cheatEnd - cheatStart).abs()

            distance <= cheatLength
                    && shortestPath.getScore(cheatEnd) - index - distance >= timeSavedGoal
        }
    }
}

