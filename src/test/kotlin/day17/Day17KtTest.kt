package day17

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day17KtTest {

    private val day = 17

    @Test
    fun sample1() {
        val text = """
        Register A: 729
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
        """.trimIndent().trimEnd()

        assertEquals("4,6,3,5,6,3,5,2,1,0", solveA(text, Debug.Disabled))
    }

    @Test
    fun sample2() {
        val text = """
        Register A: 2024
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
        """.trimIndent().trimEnd()

        val (outputs, registers) = runProgram(text, Debug.Disabled)
        assertEquals(0, registers['A'])
        assertEquals("4,2,5,6,7,7,7,7,3,1,0", outputs.joinToString(separator = ","))
    }

    @Test
    fun sample3() {
        assertEquals(
            1, runProgram(
                """
            Register A: 0
            Register B: 0
            Register C: 9
    
            Program: 2,6
            """.trimIndent().trimEnd(), Debug.Disabled
            ).second['B']
        )

        assertEquals(
            26, runProgram(
                """
            Register A: 0
            Register B: 29
            Register C: 0
    
            Program: 1,7
            """.trimIndent().trimEnd(), Debug.Disabled
            ).second['B']
        )

        assertEquals(
            44354, runProgram(
                """
            Register A: 0
            Register B: 2024
            Register C: 43690
    
            Program: 4,0
            """.trimIndent().trimEnd(), Debug.Disabled
            ).second['B']
        )

        assertEquals(
            "0,1,2", solveA(
                """
            Register A: 10
            Register B: 0
            Register C: 9
    
            Program: 5,0,5,1,5,4
            """.trimIndent().trimEnd(), Debug.Disabled
            )
        )

    }

    @Test
    fun verifySimplified() {
        //Answer from part 1
        val expectedOutput = listOf(7, 6, 5, 3, 6, 5, 7, 0, 4)

        var a = 27334280L
        var i = 0
        while (a != 0L) {
            val out = runProgramIteration(a)
            assertEquals(expectedOutput[i], out)
            a /= 8
            i++
        }
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines, Debug.Disabled)
        println("A: $solveA")
        assertEquals("7,6,5,3,6,5,7,0,4", solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(190615597431823, solveB)
    }
}