package day2

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    return text.lines().map { line -> line.split(' ').map { it.toInt() } }.count { isSafe(it) }
}

fun isSafe(report: List<Int>): Boolean {
    val zipped = report.zipWithNext()
    return zipped.all { (l, r) -> l > r && l - r <= 3 } || zipped.all { (l, r) -> l < r && r - l <= 3 }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    return text.lines().map { line -> line.split(' ').map { it.toInt() } }.count { isSafeB(it) }
}

fun isSafeB(report: List<Int>): Boolean {
    return isSafe(report) || report.indices.any {
        val newReport = report.toMutableList()
        newReport.removeAt(it)
        isSafe(newReport)
    }
}
