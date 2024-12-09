package day9

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day9KtTest {

    private val day = 9

    @Test
    fun sample1() {
        val text = """
        2333133121414131402
        """.trimIndent().trimEnd()

        assertEquals(1928, solveA(text, Debug.Disabled))
        assertEquals(2858, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(6242766523059, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(6272188244509, solveB)
    }
}