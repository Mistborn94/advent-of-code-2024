package day10

import helper.Debug
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get
import helper.point.base.points

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines().map { line -> line.map { it.digitToInt() } }
    val heads = map.points().filter { map[it] == 0 }

    return heads.sumOf { head -> endLocations(map, head, mutableMapOf()).size }
}

fun endLocations(map: List<List<Int>>, head: Point, cache: MutableMap<Point, Set<Point>>): Set<Point> {
    return if (map[head] == 9) {
        setOf(head)
    } else {
        cache.getOrPut(head) {
            head.neighbours().filter { n -> n in map && map[n] - map[head] == 1 }
                .flatMapTo(mutableSetOf()) { n -> endLocations(map, n, cache) }
        }
    }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines().map { line -> line.map { it.digitToInt() } }
    val heads = map.points().filter { map[it] == 0 }

    return heads.sumOf { head -> rating(map, head, mutableMapOf()) }
}

fun rating(map: List<List<Int>>, head: Point, cache: MutableMap<Point, Int>): Int {
    return if (map[head] == 9) {
        1
    } else {
        cache.getOrPut(head) {
            head.neighbours().filter { n -> n in map && map[n] - map[head] == 1 }
                .sumOf { n -> rating(map, n, cache) }
        }
    }
}
