package day10

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day10KtTest {

    private val day = 10

    @Test
    fun sample1() {
        val text = """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
        """.trimIndent().trimEnd()

        assertEquals(36, solveA(text, Debug.Enabled))
        assertEquals(81, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(737, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(1619, solveB)
    }
}