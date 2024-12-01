package day1

import helper.Debug
import kotlin.math.abs

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val (left, right) = parseInput(text)

    return left.sorted().zip(right.sorted()).sumOf { (l, r) -> abs(l - r) }
}

private fun parseInput(text: String): Pair<MutableList<Int>, MutableList<Int>> {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    val regex = " +".toRegex()
    text.trim().lines().map { it.split(regex) }.forEach { (l, r) ->
        left.add(l.toInt())
        right.add(r.toInt())
    }
    return Pair(left, right)
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val (left, right) = parseInput(text)
    return left.sumOf { l -> l * right.count { r -> r == l } }
}
