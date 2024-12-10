package day9

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Long {
    val fileSystem = text.toList().flatMapIndexed { index, digit ->
        buildList {
            val digitValue = digit.digitToInt()
            if (index % 2 == 0) {
                val fileId = index / 2
                repeat(digitValue) { add(fileId) }
            } else {
                repeat(digitValue) { add(null) }
            }
        }
    }.toMutableList()

    debug {
        println(print(fileSystem))
    }

    var firstNull = fileSystem.indexOf(null)
    var lastNonNull = fileSystem.indexOfLast { it != null }

    while (firstNull < lastNonNull) {
        fileSystem[firstNull] = fileSystem[lastNonNull]
        fileSystem[lastNonNull] = null

        firstNull = fileSystem.indexOf(null)
        lastNonNull = fileSystem.indexOfLast { it != null }

        debug {
            println(print(fileSystem))
        }
    }

    debug {
        println(print(fileSystem))
    }

    return fileSystem.mapIndexed { index, file -> if (file == null) 0 else index.toLong() * file }.sum()
}

private fun print(fileSystem: List<Int?>) =
    fileSystem.joinToString(separator = "") { if (it == null) "." else "$it" }

data class File(val id: Int, val start: Int, val size: Int) {
    val end = start + size

    val checksum = (start..<end).sumOf { it * id.toLong() }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val digits = text.toList().map { it.digitToInt() }

    val files = digits.drop(1).windowed(2, 2)
        .runningFoldIndexed(File(0, 0, digits[0])) { index, prev, (blankSize, fileSize) ->
            File(index + 1, prev.end + blankSize, fileSize)
        }
        .toMutableList()

    debug {
        println("S: ${printFiles(files)}")
    }

    files.reversed().forEach { file ->
        val possibleSpace = files.takeWhile { it.start <= file.start }
            .zipWithNext()
            .firstOrNull { (a, b) -> file.size <= (b.start - a.end) }

        if (possibleSpace != null) {
            val (before, after) = possibleSpace
            val index = files.indexOf(after)
            files.remove(file)
            files.add(index, file.copy(start = before.end))

            debug {
                println("${file.id}: ${printFiles(files)}")
            }
        }
    }

    debug {
        println("E: ${printFiles(files)}")
    }

    return files.sumOf { it.checksum }
}

fun printFiles(files: List<File>): String {
    var prev = 0
    return buildString {
        files.forEach { file ->
            repeat(file.start - prev) {
                append('.')
            }
            repeat(file.size) {
                append(file.id)
            }
            prev = file.end
        }
    }
}
