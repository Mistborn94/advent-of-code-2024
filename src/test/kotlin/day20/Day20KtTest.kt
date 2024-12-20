package day20

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day20KtTest {

    private val day = 20

    @Test
    fun sample1() {
        val text = """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
        """.trimIndent().trimEnd()

        assertEquals(3, solveA(text, Debug.Enabled, 38))
        assertEquals(3, solveB(text, Debug.Disabled, 76))
        assertEquals(7, solveB(text, Debug.Disabled, 74))
    }

    @Test
    @Ignore
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
        
        """.trimIndent().trimEnd()

        assertEquals(0, solveA(text, Debug.Enabled))
        assertEquals(0, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1404, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        //Not 8160
        assertEquals(1010981, solveB)
    }
}