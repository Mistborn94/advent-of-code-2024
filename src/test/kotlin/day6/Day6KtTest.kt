package day6

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day6KtTest {

    private val day = 6

    @Test
    fun sample1() {
        val text = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
        """.trimIndent().trimEnd()

        assertEquals(41, solveA(text, Debug.Enabled))
        assertEquals(6, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(5080, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(1919, solveB)
    }
}