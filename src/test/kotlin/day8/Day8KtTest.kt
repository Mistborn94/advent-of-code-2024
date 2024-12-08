package day8

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day8KtTest {

    private val day = 8

    @Test
    fun sample1() {
        val text = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
        """.trimIndent().trimEnd()

        assertEquals(14, solveA(text, Debug.Disabled))
        assertEquals(34, solveB(text, Debug.Enabled))
    }

    @Test
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
        ..........
        ..........
        ..........
        ....a.....
        ........a.
        .....a....
        ..........
        ......A...
        ..........
        ..........
        """.trimIndent().trimEnd()

        assertEquals(4, solveA(text, Debug.Enabled))
    }

    @Test
    fun sample3() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
        T.........
        ...T......
        .T........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        """.trimIndent().trimEnd()

        assertEquals(9, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(318, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(1126, solveB)
    }
}