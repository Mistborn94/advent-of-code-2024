package day23

import helper.Debug
import helper.pairwise

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val network = parseNetwork(text)

    return network.entries.filter { (k, v) -> k.startsWith("t") }
        .flatMapTo(mutableSetOf()) { (a, v) -> v.pairwise(v) { b, c -> listOf(a, b, c).sorted() } }
        .count { (a, b, c) ->
            network[a]!!.contains(b) && network[a]!!.contains(c) && network[b]!!.contains(c)
        }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): String {
    val network = parseNetwork(text)

    val possibleParties = mutableSetOf<Set<String>>()

    network.entries.forEach { (k, v) ->
        val party = mutableSetOf(k)
        val toVisit = v.toMutableList()

        while (toVisit.isNotEmpty()) {
            val next = toVisit.removeFirst()
            if (party.all { it in network[next]!! }) {
                party.add(next)
            }
        }

        possibleParties.add(party)
    }

    return possibleParties.maxBy { it.size }.sorted().joinToString(separator = ",")
}

private fun parseNetwork(text: String) = text.lines().flatMap {
    val (a, b) = it.split("-")
    listOf(a to b, b to a)
}
    .groupBy({ it.first }, { it.second })
    .mapValues { (_, v) -> v.toSet() }
