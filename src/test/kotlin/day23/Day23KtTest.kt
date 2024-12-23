package day23

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day23KtTest {

    private val day = 23

    @Test
    fun sample1() {
        val text = """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
        """.trimIndent().trimEnd()

        assertEquals(7, solveA(text, Debug.Enabled))
        assertEquals("co,de,ka,ta", solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1344, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals("ab,al,cq,cr,da,db,dr,fw,ly,mn,od,py,uh", solveB)
    }
}