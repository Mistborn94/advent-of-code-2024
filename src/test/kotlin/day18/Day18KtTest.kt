package day18

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day18KtTest {

    private val day = 18

    @Test
    fun sample1() {
        val text = """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
        """.trimIndent().trimEnd()

        assertEquals(22, solveA(text, Debug.Enabled, endIndex = 6, 12))
        assertEquals("6,1", solveB(text, Debug.Enabled, endIndex = 6))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(360, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals("58,62", solveB)
    }
}