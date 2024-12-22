package day22

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Long {
    return text.lines().sumOf {
        val seed = it.toLong()
        val last = sequenceA(seed).last()
        debug {
            println("$seed -> $last")
        }
        last
    }
}

fun Long.mix(next: Long): Long = xor(next)
fun Long.prune() = this % 16777216

fun sequenceA(start: Long): Sequence<Long> {
    return generateSequence(start) { current ->
        val first = (current * 64).mix(current).prune()
        val second = (first / 32).mix(first).prune()
        val third = (second * 2048).mix(second).prune()
        third
    }.take(2001)
}

fun sequenceB(start: Long) = sequenceA(start)
    .map { "$it".last().digitToInt() }
    .zipWithNext { a, b -> b to (b - a) }
    .windowed(4) { list -> list.map { it.second } to list.last().first }

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val (bestSequence, bestValue) = text.lines().asSequence()
        .flatMap { sequenceB(it.toLong()).distinctBy { (d, _) -> d } }
        .groupingBy { (d, _) -> d }.fold(
            initialValueSelector = { _, _ -> 0 },
            operation = { _, acc, (_, count) -> acc + count }
        ).maxBy { it.value }

    debug {
        println(bestSequence)
    }
    return bestValue
}
