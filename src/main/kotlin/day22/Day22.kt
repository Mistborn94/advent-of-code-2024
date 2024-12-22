package day22

import helper.Debug


fun Long.mix(next: Long): Long = xor(next)
fun Long.prune() = this % 16777216

fun sequenceA(start: Long) = generateSequence(start) { current ->
    val first = (current * 64).mix(current).prune()
    val second = (first / 32).mix(first).prune()
    val third = (second * 2048).mix(second).prune()
    third
}.take(2001)

fun sequenceB(start: Long): Sequence<Pair<List<Int>, Int>> = sequenceA(start)
    .map { it.toInt() % 10 }
    .zipWithNext { a, b -> b to (b - a) }
    .windowed(4) { list -> list.map { it.second } to list.last().first }

fun solveA(text: String, debug: Debug = Debug.Disabled) = text.lines()
    .sumOf { seed -> sequenceA(seed.toLong()).last() }

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val (bestSequence, bestValue) = text.lines().asSequence()
        .flatMap { sequenceB(it.toLong()).distinctBy { (d, _) -> d } }
        .groupingBy { (d, _) -> d }
        .fold(
            initialValueSelector = { _, _ -> 0 },
            operation = { _, acc, (_, count) -> acc + count }
        ).maxBy { it.value }

    debug {
        println(bestSequence)
    }
    return bestValue
}

