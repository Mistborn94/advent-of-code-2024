package day17

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): String {
    val (outputs, _) = runProgram(text, debug)

    return outputs.joinToString(separator = ",")
}

fun runProgram(
    text: String,
    debug: Debug = Debug.Disabled
): Pair<List<Int>, Map<Char, Int>> {
    val (instructionText, programText) = text.split("\n\n")
    val program = programText.substringAfter(": ").split(",").map { it.toInt() }

    val registers = instructionText.lines().associate {
        val register = it.substringAfter(' ').substringBefore(':')[0]
        val value = it.substringAfterLast(' ').trim().toInt()
        register to value
    }

    var instructionPointer = 0
    val outputs = mutableListOf<Int>()
    debug {
        println("Starting Program $program")
        println("Registers: $registers")
    }
    var a = registers['A']!!
    var b = registers['B']!!
    var c = registers['C']!!
    while (instructionPointer < program.size) {
        val opCode = program[instructionPointer]
        val operand = program[instructionPointer + 1]

        debug {
            println("Instruction $instructionPointer [$opCode, $operand]:")
        }
        when (opCode) {
            //adv
            0 -> {
                val comboOperand = comboOperand(operand, a, b, c)
                debug {
                    println("   adv $a >> $comboOperand")
                }
                a = a.shr(comboOperand)
                instructionPointer += 2
            }
            //bxl
            1 -> {
                debug {
                    println("   bxl $b xor $operand")
                }
                b = b xor operand
                instructionPointer += 2
            }
            //bst
            2 -> {
                val comboOperand = comboOperand(operand, a, b, c)
                debug {
                    println("   bst $comboOperand mod 8")
                }
                b = comboOperand.mod(8)
                instructionPointer += 2
            }
            //jnz
            3 -> {
                instructionPointer = if (a == 0) {
                    debug {
                        println("   jnz none")
                    }
                    instructionPointer + 2
                } else {
                    debug {
                        println("   jnz $operand\n\n")
                    }
                    operand
                }
            }
            //bxc
            4 -> {
                debug {
                    println("   bxc $b xor $c")
                }
                b = b xor c
                instructionPointer += 2
            }
            //out
            5 -> {
                val comboOperand = comboOperand(operand, a, b, c)

                debug {
                    println("   out $comboOperand mod 8")
                }
                outputs.add(comboOperand.mod(8))
                instructionPointer += 2
            }
            //bdv
            6 -> {
                val comboOperand = comboOperand(operand, a, b, c)
                debug {
                    println("   bdv $a >> $comboOperand")
                }
                b = a.shr(comboOperand)
                instructionPointer += 2
            }
            //cdv
            7 -> {
                val comboOperand = comboOperand(operand, a, b, c)
                debug {
                    println("   cdv $a >> $comboOperand")
                }
                c = a.shr(comboOperand)
                instructionPointer += 2
            }
        }
        debug {
            println("   Registers: $registers")
        }
    }
    return outputs to mapOf('A' to a, 'B' to b, 'C' to c)
}

fun comboOperand(operand: Int, a: Int, b: Int, c: Int): Int {
    return when (operand) {
        in 0..3 -> operand
        4 -> a
        5 -> b
        6 -> c
        else -> throw IllegalArgumentException("Invalid operand $operand")
    }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val (_, programText) = text.split("\n\n")
    val program = programText.substringAfter(": ").split(",").map { it.toInt() }

    return findDigit(program).first()
}

fun findDigit(expectedOutput: List<Int>): List<Long> {
    return if (expectedOutput.size == 1) {
        (0L..7L).filter { runProgramIteration(it) == expectedOutput.first() }
    } else {
        findDigit(expectedOutput.drop(1)).flatMap { acc ->
            (acc * 8..acc * 8 + 7).filter { runProgramIteration(it) == expectedOutput.first() }
        }
    }
}

fun runProgramIteration(a: Long): Int {
    val b1 = (a % 8) xor 2
    // shift right is equivalent to a / (2^b1)
    val c = a.shr(b1.toInt())
    val b2 = b1 xor 7 xor c
    return (b2 % 8).toInt()
}
