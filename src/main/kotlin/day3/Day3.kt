package day3

import helper.Debug

val regexA = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
val regexB = """mul\((\d{1,3}),(\d{1,3})\)|don't\(\)|do\(\)""".toRegex()

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    return regexA.findAll(text).sumOf {

        val (a, b) = it.destructured

        a.toInt() * b.toInt()

    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    var enabled = true
    return regexB.findAll(text).sumOf {
        if (it.value == "do()") {
            enabled = true
            0
        } else if (it.value == "don't()") {
            enabled = false
            0
        } else if (enabled) {
            val (a, b) = it.destructured

            a.toInt() * b.toInt()
        } else {
            0
        }
    }
}
