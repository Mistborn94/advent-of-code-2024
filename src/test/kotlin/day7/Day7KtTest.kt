package day7

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day7KtTest {

    private val day = 7

    @Test
    fun sample1() {
        val text = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
        """.trimIndent().trimEnd()

        assertEquals(3749, solveA(text, Debug.Enabled))
        assertEquals(11387, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(5702958180383, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(92612386119138, solveB)
    }
}