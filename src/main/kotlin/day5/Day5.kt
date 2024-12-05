package day5

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val (rulesText, updates) = text.split("\n\n").map { it.trim() }

    val rules = parseRules(rulesText.lines())

    return updates.lines().sumOf { line ->
        val pages = line.split(",").map { it.toInt() }
        if (isInOrder(pages, rules)) {
            pages[pages.size / 2]
        } else {
            0
        }
    }
}

private fun parseRules(rulesLines: List<String>): Map<Int, List<Int>> =
    rulesLines.map { line -> line.split("|").map(String::toInt) }.groupBy({ it[0] }, { it[1] })

fun isInOrder(ints: List<Int>, rules: Map<Int, List<Int>>): Boolean {
    return ints.indices.drop(0).all { page ->
        val previous = ints.subList(0, page)
        val current = ints[page]

        val beforeRules = rules[current] ?: emptyList()
        beforeRules.none { it in previous }
    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val (rules, updates) = text.split("\n\n").map { it.trim() }

    val before = parseRules(rules.lines())

    return updates.lines().sumOf { line ->
        val ints = line.split(",").map { it.toInt() }
        if (isInOrder(ints, before)) {
            0
        } else {
            val sorted = sortPages(ints, before)
            sorted[sorted.size / 2]
        }
    }
}

fun sortPages(pages: List<Int>, rules: Map<Int, List<Int>>): List<Int> {
    val ruleCounts = pages.associateWith { page -> (rules[page] ?: emptyList()).count { it in pages } }
    return pages.sortedWith(compareBy { ruleCounts[it] ?: 0 })
}
