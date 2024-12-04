package day4

import helper.Debug
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get

val word = "XMAS".toRegex()
val reversed = "XMAS".reversed().toRegex()

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val lines = text.lines()
    val rowCount = findWords(lines)
    val columns = buildColumns(lines)
    val columnCount = findWords(columns)

    val diag1 = diagonalDownRightPoints(lines).map {
        it.joinToString(separator = "") { p -> lines[p].toString() }
    }
    val diag2 = diagonalDownLeftPoints(lines).map {
        it.joinToString(separator = "") { p -> lines[p].toString() }
    }

    val diag1Count = findWords(diag1)
    val diag2Count = findWords(diag2)

    debug {
        println("Counts")
        println("Rows: $rowCount")
        println("Colums: $columnCount")
        println("Diagonals: $diag1Count")
        println("Diagonals: $diag2Count")
    }

    return rowCount + columnCount + diag1Count + diag2Count
}

private fun diagonalDownLeftPoints(lines: List<String>) = buildSet {
    this.addAll(lines[0].indices.map { Point(it, 0) })
    this.addAll(lines.indices.map { Point(lines[0].lastIndex, it) })
}.map {
    var pos = it
    buildList {
        while (pos in lines) {
            add(pos)
            pos += Point(-1, 1)
        }
    }
}

private fun diagonalDownRightPoints(lines: List<String>) = buildSet {
    this.addAll(lines.indices.map { Point(it, 0) })
    this.addAll(lines[0].indices.map { Point(0, it) })
}.map {
    var pos = it
    buildList {
        while (pos in lines) {
            add(pos)
            pos += Point(1, 1)
        }
    }
}

private fun findWords(lines: List<String>) = lines.sumOf {
    word.findAll(it).count() + reversed.findAll(it).count()
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val lines = text.lines()

    val diagonalDownRightPoints = diagonalDownRightPoints(lines)
    val diagonalDownLeftPoints = diagonalDownLeftPoints(lines)

    val diag1Points = findWordLocations(lines, diagonalDownRightPoints)
    val diag2Points = findWordLocations(lines, diagonalDownLeftPoints)

    return diag1Points.intersect(diag2Points).size
}

private fun buildColumns(lines: List<String>): List<String> = buildList {
    for (i in lines[0].indices) {
        add(buildString {
            for (line in lines) {
                append(line[i])
            }
        })
    }
}

private fun findWordLocations(lines: List<String>, points: List<List<Point>>) =
    points.flatMapTo(mutableSetOf()) {
        it.windowed(3).filter { (a, b, c) ->
            lines[b] == 'A' && ((lines[a] == 'M' && lines[c] == 'S') || (lines[a] == 'S' && lines[c] == 'M'))
        }.map { (_, b, _) -> b }
    }