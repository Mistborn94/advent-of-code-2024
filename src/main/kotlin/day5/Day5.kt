package day5

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val (rulesText, updates) = text.split("\n\n").map { it.trim() }

    val rules = parseRules(rulesText.lines())

    return updates.lines().sumOf { line ->
        val ints = line.split(",").map { it.toInt() }
        if (isInOrder(ints, rules)) {
            ints[ints.size / 2]
        } else {
            0
        }
    }
}

private fun parseRules(rulesLines: List<String>): Map<Int, List<Int>> {
    val rules = mutableMapOf<Int, MutableList<Int>>()

    rulesLines.forEach { line ->
        val (first, second) = line.split("|").map { it.toInt() }

        rules.getOrPut(first) { mutableListOf() }.add(second)
    }
    return rules
}

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

fun sortPages(ints: List<Int>, rules: Map<Int, List<Int>>): List<Int> {
    val unsorted = ints.toMutableList()
    val sorted = mutableListOf<Int>()
    while (unsorted.isNotEmpty()) {
        val next = unsorted.removeAt(0)
        val afterNext = rules[next] ?: emptyList()

        val index = sorted.indexOfFirst { it in afterNext }
        if (index == -1) {
            sorted.add(next)
        } else {
            sorted.add(index, next)
        }
    }

    return sorted
}
