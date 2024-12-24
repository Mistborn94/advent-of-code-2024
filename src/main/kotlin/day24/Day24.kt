package day24

import helper.Debug
import helper.pairwise

val regex = "([^ ]+) (XOR|OR|AND) ([^ ]+) -> ([^ ]+)".toRegex()

fun solveA(text: String, debug: Debug = Debug.Disabled): Long {
    val (wires, gates) = parseInput(text)

    return findSwap(gates, wires)!!
}

private fun findSwap(
    inputGates: Collection<LogicGate>,
    inputWires: Map<String, Boolean>,
): Long? {
    val gates = inputGates.toMutableSet()
    val wires = inputWires.toMutableMap()
    while (gates.isNotEmpty()) {
        val gate = gates.firstOrNull { it.input1 in wires && it.input2 in wires }
        if (gate == null) {
            return null
        }
        gates.remove(gate)

        wires[gate.output] = gate.operator(wires[gate.input1]!!, wires[gate.input2]!!)
    }

    return longFromPrefix(wires, "z")
}

private fun longFromPrefix(wires: Map<String, Boolean>, prefix: String) =
    wires.filterKeys { k -> k.startsWith(prefix) }
        .toList()
        .sortedByDescending { (k, _) -> k }
        .map { (_, v) -> if (v) 1 else 0 }
        .fold(0L) { a, b -> a * 2 + b }

data class LogicGate(
    val input1: String,
    val input2: String,
    val operatorName: String,
    val output: String
) {
    val operator: (Boolean, Boolean) -> Boolean = when (operatorName) {
        "AND" -> { t, u -> t && u }
        "OR" -> { t, u -> t || u }
        "XOR" -> { t, u -> t != u }
        else -> throw IllegalArgumentException("Unknown operator: $operatorName")
    }

    override fun toString(): String {
        return "$input1 $operatorName $input2 -> $output"
    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): String {
    val (wires, gates) = parseInput(text)
    val xInput = longFromPrefix(wires, "x")
    val yInput = longFromPrefix(wires, "y")
    val expectedOutput = xInput + yInput

    val maxBitCount = wires.keys.filter { it.startsWith("x") }.size

    val craftedInput = craftInput(maxBitCount, xInput, yInput)
    check(craftedInput == wires) {
        "$craftedInput != $wires"
    }

    val (sum0, carry0) = gates.findHalfAdder("x00", "y00")

    check(sum0!!.output == "z00") { "First half adder correct " }
    var carryOut = carry0!!.output

    debug {
        println(
            """Found Half-Adder for bit 0:
                | Sum  : $sum0
                | Carry: $carry0
            """.trimMargin()
        )
    }

    var remappedLogicGates = gates
    var knownSwaps = mutableSetOf<String>()
    debug {
        println("=== Testing Simulation ===")
    }
    var swap = findSwap(maxBitCount, remappedLogicGates, carryOut, debug)
    while (swap != null) {
        val (o1, o2) = swap
        knownSwaps.add(o1)
        knownSwaps.add(o2)

        remappedLogicGates = remappedLogicGates.map {
            when (it.output) {
                o1 -> it.copy(output = o2)
                o2 -> it.copy(output = o1)
                else -> it
            }
        }
        debug {
            println("=== Testing Simulation ===")
            println("Known Swaps are: $knownSwaps")
        }
        swap = findSwap(maxBitCount, remappedLogicGates, carryOut, debug)
    }

    check(knownSwaps.size == 8) {
        "There must be exactly 7 swapped wires"
    }

    return knownSwaps.sorted().joinToString(separator = ",")
}

private fun findSwap(
    maxBitCount: Int,
    gates: List<LogicGate>,
    firstCarry: String,
    debug: Debug
): Pair<String, String>? {
    var previousCarry = firstCarry
    for (i in 1..<maxBitCount) {
        val inputIndex = "$i".padStart(2, '0')
        val x = "x$inputIndex"
        val y = "y$inputIndex"
        val z = "z$inputIndex"

        val (sum1, carry1) = gates.findHalfAdder(x, y)
        check(sum1 != null && carry1 != null) {
            "Unfixable Situation: [$x AND $y] or [$x XOR $y] missing"
        }

        debug {
            println(
                """
                |Found Adder for bit $i:
                | Sum   1: $sum1
                | Carry 1: $carry1
            """.trimMargin()
            )
        }

        val intermediate1 = previousCarry
        val intermediate2 = sum1.output
        val (sum2, carry2) = gates.findHalfAdder(intermediate1, intermediate2)
        debug {
            println(
                """
                | Sum   2: $sum2
                | Carry 2: $carry2
            """.trimMargin()
            )
        }

        if (sum2 == null) {
            val sum2GateByOutput = gates.first { it.output == z }
            debug {
                println("Sum 2: $intermediate1 XOR $intermediate2 not found. Problem with $intermediate1 or $intermediate2")
                println("Correct gate: $sum2GateByOutput")
            }
            val correctInputs = setOf(sum2GateByOutput.input1, sum2GateByOutput.input2)

            check(intermediate1 in correctInputs || intermediate2 in correctInputs) { "$intermediate1 or $intermediate2 must be inputs to the correct gate" }
            return if (intermediate1 in correctInputs) {
                intermediate2 to correctInputs.first { it != intermediate1 }
            } else {
                intermediate1 to correctInputs.first { it != intermediate2 }
            }
        }

        if (sum2.output != z) {
            debug {
                println("Found problem: [$sum2] output should be $z ")
            }
            return sum2.output to z
        }

        checkNotNull(carry2) {
            "Unfixable situation: [$intermediate1 AND $intermediate2] not found"
        }

        val carry = gates.findGate(carry1.output, carry2.output, "OR")
        debug {
            println("Carry: $carry")
        }
        checkNotNull(carry) {
            "Unfixable situation: [${carry1.output} OR ${carry2.output}] not found."
        }
        check(!(carry.output.startsWith("z") && i < maxBitCount - 1)) {
            "Unfixable situation: Carry should not output to z - ${carry.output}"
        }
        previousCarry = carry.output
    }

    return null
}

private fun Collection<LogicGate>.findHalfAdder(a: String, b: String): Pair<LogicGate?, LogicGate?> {
    val sum = findGate(a, b, "XOR")
    val carry = findGate(a, b, "AND")
    return Pair(sum, carry)
}

private fun Collection<LogicGate>.findGate(
    a: String,
    b: String,
    type: String
): LogicGate? {
    val in1 = minOf(a, b)
    val in2 = maxOf(a, b)
    return firstOrNull { it.input1 == in1 && it.input2 == in2 && it.operatorName == type }
}

fun craftInput(bitCount: Int, xIn: Long, yIn: Long): Map<String, Boolean> {
    var bit = 0
    var x = xIn
    var y = yIn
    return buildMap {
        while (bit < bitCount) {
            val xBit = x % 2 == 1L
            val yBit = y % 2 == 1L
            val paddedBit = "$bit".padStart(2, '0')
            put("x$paddedBit", xBit)
            put("y$paddedBit", yBit)
            x /= 2
            y /= 2
            bit++
        }
    }
}

fun searchPairs(
    gates: List<LogicGate>,
    wires: Map<String, Boolean>,
    expectedOutput: Long,
    debug: Debug
): String {
    val pairsOfIndices = gates.indices.pairwise(gates.indices)
    debug {
        println("Pairs of indices: $pairsOfIndices")
    }
    pairsOfIndices.forEach { (ai1, ai2) ->
        val a = gates[ai1] to gates[ai2]
        val seen = setOf(ai1, ai2)
        debug {
            println("Testing A: $ai1, $ai2")
        }
        pairsOfIndices.forEach { (bi1, bi2) ->
            if (bi1 !in seen && bi2 !in seen) {
                val b = gates[bi1] to gates[bi2]
                val seen = buildSet {
                    this.addAll(seen)
                    this.add(bi1)
                    this.add(bi2)
                }
                pairsOfIndices.forEach { (ci1, ci2) ->
                    if (ci1 !in seen && ci2 !in seen) {
                        val c = gates[ci1] to gates[ci2]
                        val seen = buildSet {
                            this.addAll(seen)
                            this.add(ci1)
                            this.add(ci2)
                        }
                        pairsOfIndices.forEach { (di1, di2) ->
                            if (di1 !in seen && di2 !in seen) {
                                val d = gates[di1] to gates[di2]

                                val outputString = outputString(a, b, c, d)
                                val newGates = gates.toMutableSet()
                                val chosenGates = listOf(a, b, c, d)
                                chosenGates.forEach { (first, second) ->
                                    newGates.remove(first)
                                    newGates.remove(second)
                                    newGates.add(first.copy(output = second.output))
                                    newGates.add(second.copy(output = first.output))
                                }

                                val runSimulation = findSwap(newGates, wires)
                                debug {
                                    if (runSimulation == expectedOutput) {
                                        println("$outputString -> $runSimulation")
                                    }
                                }
                                if (runSimulation == expectedOutput) {
                                    return outputString
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    throw IllegalStateException("No solution found")
}

fun parseInput(text: String): Pair<Map<String, Boolean>, List<LogicGate>> {
    val (wiresText, gatesText) = text.split("\n\n")
    val wires = wiresText.lines().associate { line ->
        val (first, second) = line.split(": ")
        first.trim() to (second.trim() == "1")
    }
    val gates = gatesText.lines().map { line ->
        val (in1, operator, in2, out) = regex.matchEntire(line)!!.destructured
        LogicGate(minOf(in1, in2), maxOf(in1, in2), operator, out)
    }
    val parsed = Pair(wires, gates)
    return parsed
}

private fun outputString(
    a: Pair<LogicGate, LogicGate>,
    b: Pair<LogicGate, LogicGate>,
    c: Pair<LogicGate, LogicGate>,
    d: Pair<LogicGate, LogicGate>
) = buildList {
    add(a.first.output)
    add(a.second.output)
    add(b.first.output)
    add(b.second.output)
    add(c.first.output)
    add(c.second.output)
    add(d.first.output)
    add(d.second.output)
}.sorted().joinToString(separator = ",")
