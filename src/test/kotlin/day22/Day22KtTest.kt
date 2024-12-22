package day22

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day22KtTest {

    private val day = 22

    @Test
    fun sample1() {
        val text = """
        1
        10
        100
        2024
        """.trimIndent().trimEnd()

        assertEquals(37327623, solveA(text, Debug.Enabled))
    }

    @Test
    fun sample3() {
        val text = """
        1
        2
        3
        2024
        """.trimIndent().trimEnd()

        assertEquals(23, solveB(text, Debug.Enabled))
    }

    @Test
    fun sample2() {

        val expectedSequence = """
            15887950
            16495136
            527345
            704524
            1553684
            12683156
            11100544
            12249484
            7753432
            5908254""".trimIndent()
        val expectedA = expectedSequence.lines().map { it.toLong() }
        assertEquals(expectedA, sequenceA(123L).drop(1).take(10).toList())

        val expectedB = listOf(
            listOf(-3, 6, -1, -1) to 4,
            listOf(6, -1, -1, 0) to 4,
            listOf(-1, -1, 0, 2) to 6,
            listOf(-1, 0, 2, -2) to 4,
            listOf(0, 2, -2, 0) to 4,
            listOf(2, -2, 0, -2) to 2,
        )

        assertEquals(expectedB, sequenceB(123L).take(6).toList())
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(17262627539, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        //not 2121
        assertEquals(1986, solveB)
    }
}