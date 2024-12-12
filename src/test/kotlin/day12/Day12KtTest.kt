package day12

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day12KtTest {

    private val day = 12

    @Test
    fun sample1() {
        val text = """
        AAAA
        BBCD
        BBCC
        EEEC
        """.trimIndent().trimEnd()

        assertEquals(140, solveA(text, Debug.Enabled))
        assertEquals(80, solveB(text, Debug.Enabled))
    }

    @Test
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
        """.trimIndent().trimEnd()

        assertEquals(1930, solveA(text, Debug.Enabled))
        assertEquals(1206, solveB(text, Debug.Disabled))
    }

    @Test
    fun sample3() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
        """.trimIndent().trimEnd()

        assertEquals(772, solveA(text, Debug.Enabled))
        assertEquals(436, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1375476, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(821372, solveB)
    }
}