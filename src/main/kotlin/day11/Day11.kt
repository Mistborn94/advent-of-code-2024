package day11

import helper.Debug

// Original solution for part 1
fun solveA(text: String, debug: Debug = Debug.Disabled, reps: Int = 25): Int {
    var list = text.split(" ").map { it.toLong() }
    repeat(reps) {
        list = list.flatMap { stone ->
            if (stone == 0L) {
                listOf(1)
            } else {
                val str = "$stone"
                if (str.length % 2 == 0) {
                    listOf(
                        str.substring(0..<str.length / 2).toLong(),
                        str.substring(str.length / 2).toLong()
                    )
                } else {
                    listOf(stone * 2024)
                }
            }
        }
    }
    return list.size
}

fun solve(text: String, debug: Debug = Debug.Disabled, reps: Int): Long {
    val list = text.split(" ").map { it.toLong() }
    val cache = (1..reps).associateWith { mutableMapOf<Long, Long>() }

    return list.sumOf { countStones(it, cache, reps) }
}

fun countStones(stone: Long, cache: Map<Int, MutableMap<Long, Long>>, rep: Int): Long {
    return if (rep == 0) {
        1
    } else {
        val iterationCache = cache[rep]!!
        iterationCache.getOrPut(stone) {
            val str = "$stone"
            if (stone == 0L) {
                countStones(1, cache, rep - 1)
            } else if (str.length % 2 == 0) {
                val first = str.substring(0..<str.length / 2).toLong()
                val second = str.substring(str.length / 2).toLong()

                countStones(first, cache, rep - 1) + countStones(second, cache, rep - 1)
            } else {
                countStones(stone * 2024, cache, rep - 1)
            }
        }
    }
}
