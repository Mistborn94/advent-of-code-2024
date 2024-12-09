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
        println(printFs(fileSystem))
    }

    var firstNull = fileSystem.indexOf(null)
    var lastNonNull = fileSystem.indexOfLast { it != null }

    while (firstNull < lastNonNull) {
        fileSystem[firstNull] = fileSystem[lastNonNull]
        fileSystem[lastNonNull] = null

        firstNull = fileSystem.indexOf(null)
        lastNonNull = fileSystem.indexOfLast { it != null }

        debug {
            println(printFs(fileSystem))
        }
    }

    debug {
        println(printFs(fileSystem))
    }

    return checksum(fileSystem)
}

private fun checksum(fileSystem: MutableList<Int?>) =
    fileSystem.mapIndexed { index, file -> if (file == null) 0 else index.toLong() * file }.sum()

private fun printFs(fileSystem: MutableList<Int?>) =
    fileSystem.joinToString(separator = "") { if (it == null) "." else "$it" }


fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val digits = text.toList().map { it.digitToInt() }
    //File id to size
    val files = digits.mapIndexedNotNull { index, size -> if (index % 2 == 0) index / 2 to size else null }
    //size to next file
    val freeSpace =
        digits.mapIndexedNotNull { index, size -> if (index % 2 == 0) null else size to (index / 2 + 1) }
            .toMutableList()

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

    files.reversed().forEach { (fileId, fileSize) ->
        val possibleSpace =
            freeSpace.takeWhile { (_, next) -> next <= fileId }.firstOrNull { (spaceSize, _) -> spaceSize >= fileSize }

        if (possibleSpace != null) {
            val freeSpaceIndex = freeSpace.indexOf(possibleSpace)
            freeSpace.remove(possibleSpace)
            val (spaceSize, nextFile) = possibleSpace

            val currentPosition = fileSystem.indexOf(fileId)
            val newPosition = fileSystem.indexOf(nextFile) - spaceSize
            repeat(fileSize) {
                fileSystem[currentPosition + it] = null
                fileSystem[newPosition + it] = fileId
            }

            if (spaceSize > fileSize) {
                freeSpace.add(freeSpaceIndex, (spaceSize - fileSize) to nextFile)
            }
        }

        debug {
            println("$fileId: ${printFs(fileSystem)}")
        }
    }

    debug {
        println(printFs(fileSystem))
    }

    return checksum(fileSystem)
}
