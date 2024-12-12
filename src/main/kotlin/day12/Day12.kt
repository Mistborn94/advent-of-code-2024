package day12

import helper.Debug
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
        val symbol = map[start]

        val regionToVisit = mutableSetOf(start)
        val regionPoints = mutableSetOf<Point>()
        val horizontalEdges = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        val verticalEdges = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

        while (regionToVisit.isNotEmpty()) {
            val current = regionToVisit.removeFirst()
            if (visited.add(current)) {
                regionPoints.add(current)
                current.neighbours().forEach { next ->
                    if (next !in map || map[next] != symbol) {
                        val d = next - current
                        if (next.x == current.x) {
                            horizontalEdges.getOrPut(current.y to d.y) { mutableListOf() }.add(next.x)
                        } else {
                            verticalEdges.getOrPut(current.x to d.x) { mutableListOf() }.add(next.y)
                        }
                    }
                    if (next in map) {
                        if (map[next] != symbol) {
                            toVisit.add(next)
                        } else {
                            regionToVisit.add(next)
                        }
                    }
                }
            }
        }
        total += if (b) {
            regionPoints.size * (horizontalEdges.values.sumOf { it.continuousGroups() } + verticalEdges.values.sumOf { it.continuousGroups() })
        } else {
            regionPoints.size * (horizontalEdges.values.sumOf { it.size } + verticalEdges.values.sumOf { it.size })
        }
    }
    return total
}


private fun List<Int>.continuousGroups(): Int = sorted().zipWithNext().count { (a, b) -> b - a != 1 } + 1