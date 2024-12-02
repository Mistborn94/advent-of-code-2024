package day2

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day2KtTest {

    private val day = 2

    @Test
    fun sample1() {
        val text = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """.trimIndent().trimEnd()

        assertEquals(2, solveA(text, Debug.Enabled))
        assertEquals(4, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(472, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(520, solveB)
    }
}