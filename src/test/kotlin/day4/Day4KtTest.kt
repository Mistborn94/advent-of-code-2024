package day4

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day4KtTest {

    private val day = 4

    @Test
    fun sample1() {
        val text = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent().trimEnd()

        assertEquals(18, solveA(text, Debug.Enabled))
        assertEquals(9, solveB(text, Debug.Enabled))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(2583, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(1978, solveB)
    }
}