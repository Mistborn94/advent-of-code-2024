package day7

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Long {
    val operators = listOf<(Long, Long) -> Long>({ a, b -> a + b }, { a, b -> a * b })

    val data = parseInput(text)
    return data.sumOf { (answer, operands) ->
        if (solveable(answer, operands, operators)) answer else 0
    }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val operators = listOf<(Long, Long) -> Long>(
        { a, b -> a + b },
        { a, b -> a * b },
        { a, b -> "$a$b".toLong() },
    )

    val data = parseInput(text)
    return data.sumOf { (answer, operands) ->
        if (solveable(answer, operands, operators)) answer else 0
    }
}

private fun parseInput(text: String) = text.lines().map { line ->
    val answer = line.substringBefore(":").toLong()
    val operands = line.substringAfter(": ").split(' ').map(String::toLong)
    answer to operands
}

fun solveable(answer: Long, operands: List<Long>, operators: List<(Long, Long) -> Long>): Boolean {
    return if (operands.size == 1) {
        operands[0] == answer
    } else {
        operators.any { operator ->
            val newOperands = buildList {
                add(operator(operands[0], operands[1]))
                addAll(operands.subList(2, operands.size))
            }
            solveable(answer, newOperands, operators)
        }
    }
}
