package day11

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day11KtTest {

    private val day = 11

    @Test
    fun sample1() {
        val text = """
        125 17
        """.trimIndent().trimEnd()

        assertEquals(2, solve(text, Debug.Enabled, 0))
        assertEquals(3, solve(text, Debug.Enabled, 1))
        assertEquals(4, solve(text, Debug.Enabled, 2))
        assertEquals(5, solve(text, Debug.Enabled, 3))
        assertEquals(9, solve(text, Debug.Enabled, 4))
        assertEquals(13, solve(text, Debug.Enabled, 5))
        assertEquals(22, solve(text, Debug.Enabled, 6))
        assertEquals(55312, solve(text, Debug.Enabled, 25))
    }

    @Test
    fun sample2() {
        val text = """
        0 1 10 99 999
        """.trimIndent().trimEnd()

        assertEquals(7, solve(text, Debug.Enabled, 1))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solve(lines, reps = 25)
        println("A: $solveA")
        assertEquals(222461, solveA)

        val solveB = solve(lines, reps = 75)
        println("B: $solveB")
        assertEquals(264350935776416, solveB)
    }
}