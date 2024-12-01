package day1

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day1KtTest {

    private val day = 1

    @Test
    fun sample1() {
        val text = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent().trimEnd()

        assertEquals(11, solveA(text, Debug.Enabled))
        assertEquals(31, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1506483, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(23126924, solveB)
    }
}