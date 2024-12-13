package day13

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Long = text.split("\n\n").sumOf { minimumTokens(it, debug) }
fun solveB(text: String, debug: Debug = Debug.Disabled): Long =
    text.split("\n\n").sumOf { minimumTokens(it, debug, true) }

val buttonPattern = """Button [AB]: X\+(\d+), Y\+(\d+)""".toRegex()
val prizePattern = """Prize: X=(\d+), Y=(\d+)""".toRegex()
fun minimumTokens(block: String, debug: Debug, part2: Boolean = false): Long {
    val lines = block.lines()
    val (ax, ay) = buttonPattern.matchEntire(lines[0])!!.groupValues.drop(1).map { it.toLong() }
    val (bX, bY) = buttonPattern.matchEntire(lines[1])!!.groupValues.drop(1).map { it.toLong() }
    val (px, py) = prizePattern.matchEntire(lines[2])!!.groupValues.drop(1).map {
        if (part2) it.toLong() + 10000000000000 else it.toLong()
    }

    val b = (py * ax - px * ay) / (bY * ax - bX * ay)
    val a = (px - bX * b) / ax

    debug {
        println("A: $a, B: $b")
    }

    val checkAnswer = a > 0
            && b > 0
            && a * ax + b * bX == px
            && a * ay + b * bY == py

    return if (checkAnswer) (b + a * 3) else 0
}

