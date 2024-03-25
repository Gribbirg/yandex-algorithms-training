package binarysearch.newofficeplus

import java.io.File
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    println(
        measureTime {
            mainFun()
        }
    )
}

fun mainFun() {
    val input = File("input.txt").bufferedReader()
    val output = File("output.txt")

    val (height, width) = input.readLine().split(' ').map { it.toInt() }
    val field = List(height) { input.readLine().map { if (it == '#') 1 else 0 } }


    val sums = List(height + 1) { MutableList(width + 1) { 0 } }
    for (lineIndex in 1..height) {
        for (charIndex in 1..width) {
            sums[lineIndex][charIndex] =
                sums[lineIndex][charIndex - 1] +
                        sums[lineIndex - 1][charIndex] -
                        sums[lineIndex - 1][charIndex - 1] +
                        field[lineIndex - 1][charIndex - 1]
        }
    }

    val maxSize = min(width, height) / 3

    var currentSize = 1
    var lineIndex = 1

    while (lineIndex <= height) {
        var charIndex = currentSize
        while (charIndex <= width) {
            if (checkPlus(sums, lineIndex, charIndex, currentSize)) {
                var l = currentSize
                var r = maxSize
                while (l < r) {
                    val medium = (l + r + 1) / 2
                    if (checkPlus(sums, lineIndex, charIndex, medium)) {
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

    output.writeText((currentSize - 1).toString())

}

fun checkPlus(sums: List<List<Int>>, startLine: Int, startChar: Int, size: Int): Boolean =
    startLine - size >= 0 && startLine + size * 2 < sums.size &&
            startChar - size >= 0 && startChar + size * 2 < sums[startLine].size &&
            checkSquare(sums, startLine, startChar - size, size, size * 3) &&
            checkSquare(sums, startLine - size, startChar, size * 3, size)

fun checkSquare(
    sums: List<List<Int>>,
    startLine: Int,
    startChar: Int,
    sizeLine: Int,
    sizeChar: Int
): Boolean =
    sums[startLine][startChar] +
            sums[startLine + sizeLine][startChar + sizeChar] -
            sums[startLine][startChar + sizeChar] -
            sums[startLine + sizeLine][startChar] == sizeLine * sizeChar
