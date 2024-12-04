package day4

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val lines = text.lines()
    val rRange = 0..lines.lastIndex
    val cRange = 0..lines[0].lastIndex
    return rRange.sumOf { r ->
        cRange.sumOf { c ->
            val h = if (c + 3 in cRange) "${lines[r][c]}${lines[r][c + 1]}${lines[r][c + 2]}${lines[r][c + 3]}" else ""
            val v = if (r + 3 in rRange) "${lines[r][c]}${lines[r + 1][c]}${lines[r + 2][c]}${lines[r + 3][c]}" else ""
            val tlbr =
                if (c + 3 in cRange && r + 3 in rRange) "${lines[r][c]}${lines[r + 1][c + 1]}${lines[r + 2][c + 2]}${lines[r + 3][c + 3]}" else ""
            val bltr =
                if (c + 3 in cRange && r + 3 in rRange) "${lines[r + 3][c]}${lines[r + 2][c + 1]}${lines[r + 1][c + 2]}${lines[r][c + 3]}" else ""

            listOf(h, v, tlbr, bltr).count { it == "XMAS" || it == "SAMX" }
        }
    }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val lines = text.lines()
    return (1..<lines.lastIndex).sumOf { r ->
        (1..<lines[0].lastIndex).count { c ->
            val tlbr = "${lines[r - 1][c - 1]}${lines[r][c]}${lines[r + 1][c + 1]}"
            val bltr = "${lines[r - 1][c + 1]}${lines[r][c]}${lines[r + 1][c - 1]}"

            (tlbr == "MAS" || tlbr == "SAM") && (bltr == "MAS" || bltr == "SAM")
        }
    }
}

