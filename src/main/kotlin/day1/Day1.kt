package day1

import helper.Debug
import kotlin.math.abs

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val (left, right) = parseInput(text)

    return left.sorted().zip(right.sorted()).sumOf { (l, r) -> abs(l - r) }
}

private fun parseInput(text: String): Pair<List<Int>, List<Int>> {
    return text.lines().map {
        val left = it.substringBefore(" ").toInt()
        val right = it.substringAfterLast(" ").toInt()
        left to right
    }.unzip()
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val (left, right) = parseInput(text)
    return left.sumOf { l -> l * right.count { r -> r == l } }
}
