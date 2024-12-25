package day25

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {

    val keys = mutableListOf<List<Int>>()
    val locks = mutableListOf<List<Int>>()

    text.split("\n\n").forEach { block ->
        val lines = block.lines()
        val columnIndices = 0..<5
        val list = columnIndices.map { x ->
            lines.count { it[x] == '#' }
        }

        check(block.first() == '#' || block.first() == '.') {
            "Bad split - block must start with '#' or '.' not '${block.first()}'"
        }

        if (block.first() == '#') {
            debug {
                println("Lock: $list")
            }
            locks.add(list)
        } else {
            debug {
                println("Key: $list")
            }
            keys.add(list)
        }
    }

    return keys.sumOf { key -> locks.count { lock -> lock.zip(key) { a, b -> a + b }.none { it > 7 } } }
}