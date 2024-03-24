package binarysearch

import java.io.File
import kotlin.math.min

fun main() {
    val input = File("input.txt").bufferedReader()
//    val output = File("output.txt")

    val (height, width) = input.readLine().split(' ').map { it.toInt() }
    val field = List(height) {
        input.readLine().toCharArray()
    }
    val maxSize = min(width, height) / 3 + 3

    var currentSize = 1
    var lineIndex = currentSize
    while (lineIndex <= height - currentSize * 2 + 1) {
        var charIndex = currentSize
        while (charIndex <= width - currentSize * 2 + 1) {
            if (checkPlus(field, lineIndex, charIndex, currentSize)) {
                var l = currentSize
                var r =  maxSize
                while (l < r) {
                    val medium = (l + r + 1) / 2
                    if (checkPlus(field, lineIndex, charIndex, medium)) {
                        l = medium
                    } else {
                        r = medium - 1
                    }
                }
                currentSize = l + 1
            }
            charIndex++
        }
        lineIndex++
    }

    println(currentSize - 1)
}

fun checkPlus(field: List<CharArray>, startLine: Int, startChar: Int, size: Int): Boolean =
    checkSquare(field, startLine, startChar, size) &&
            checkSquare(field, startLine - size, startChar, size) &&
            checkSquare(field, startLine, startChar - size, size) &&
            checkSquare(field, startLine + size, startChar, size) &&
            checkSquare(field, startLine, startChar + size, size)

fun checkSquare(field: List<CharArray>, startLine: Int, startChar: Int, size: Int): Boolean {
    if (!(
                startLine >= 0 && startLine + size <= field.size &&
                        startChar >= 0 && startChar + size <= field[startLine].size
                )
    )
        return false

    for (i in startLine..<startLine + size) {
        for (j in startChar..<startChar + size) {
            if (field[i][j] != '#')
                return false
        }
    }
    return true
}