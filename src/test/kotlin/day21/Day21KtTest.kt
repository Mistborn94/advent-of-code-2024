package day21

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day21KtTest {

    private val day = 21

    @Test
    fun sample1() {
        val text = """
        029A
        980A
        179A
        456A
        379A
        """.trimIndent().trimEnd()

        assertEquals(126384, solveA(text, Debug.Enabled))
    }

    @Test
    fun sample2() {
        assertEquals(1972, solveA("""029A""", Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(94284, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(116821732384052, solveB)
    }
}