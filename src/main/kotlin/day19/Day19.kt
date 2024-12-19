package day19

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val (patterns, designs) = text.split("\n\n")
        .let { (first, second) -> first.split(", ") to second.lines() }

    return designs.count { possible(patterns, it) }
}

fun possible(patterns: List<String>, design: String): Boolean {
    return if (design.isEmpty()) {
        true
    } else {
        patterns.any { pattern -> design.startsWith(pattern) && possible(patterns, design.drop(pattern.length)) }
    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val (patterns, designs) = text.split("\n\n")
        .let { (first, second) -> first.split(", ") to second.lines() }

    return designs.sumOf { possibleCount(patterns, it, mutableMapOf()) }
}

fun possibleCount(patterns: List<String>, design: String, cache: MutableMap<String, Long>): Long {
    return cache.getOrPut(design) {
        if (design.isEmpty()) {
            1
        } else {
            patterns.filter { design.startsWith(it) }
                .sumOf { pattern -> possibleCount(patterns, design.drop(pattern.length), cache) }
        }
    }
}

