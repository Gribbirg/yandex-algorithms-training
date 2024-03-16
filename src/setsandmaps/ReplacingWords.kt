package setsandmaps

import java.io.File

fun main() {
    val input = File("input.txt").bufferedReader()
    val fileOutput = File("output.txt")
    val dict = input.readLine().split(' ').toSet()

    fileOutput.writeText(
        input
            .readLine()
            .split(' ')
            .joinToString(" ") { word ->
                val current = StringBuilder()
                for (char in word) {
                    current.append(char)
                    if (current.toString() in dict)
                        return@joinToString current.toString()
                }
                return@joinToString word
            }
    )
}