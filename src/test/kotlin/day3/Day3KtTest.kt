package day3

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day3KtTest {

    private val day = 3

    @Test
    fun sample1() {

        assertEquals(
            161,
            solveA("""xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5)""", Debug.Enabled)
        )
        assertEquals(
            48,
            solveB("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))", Debug.Enabled)
        )
    }

    @Test
    @Ignore
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
            |
        """.trimMargin().trimEnd()

        assertEquals(0, solveA(text, Debug.Enabled))
        assertEquals(0, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(182619815, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(80747545, solveB)
    }
}