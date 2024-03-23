package binarysearch

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = File("input.txt").bufferedReader()
    val output = File("output.txt")

    val (width, n1, n2) = input.readLine().split(' ').map { it.toInt() }
    val words1 = input.readLine().split(' ').map { it.toInt() }
    val words2 = input.readLine().split(' ').map { it.toInt() }

    val prefixSums1 = MutableList(n1 + 1) { 1L }
    for (i in 0..words1.lastIndex) {
        prefixSums1[i + 1] = prefixSums1[i] + words1[i] + 1
    }
    val prefixSums2 = MutableList(n2 + 1) { 1L }
    for (i in 0..words2.lastIndex) {
        prefixSums2[i + 1] = prefixSums2[i] + words2[i] + 1
    }
    val left = words1.max()
    val right = width - words2.max()

    var r = right
    var l = left
    while (l < r) {
        val medium = (l + r) / 2
        if (getLinesCount(prefixSums1, medium) < getLinesCount(prefixSums2, width - medium)) {
            r = medium
        } else {
            l = medium + 1
        }
    }
    output.writeText(
        if (l != left)
            min(
                max(getLinesCount(prefixSums1, l), getLinesCount(prefixSums2, width - l)),
                max(getLinesCount(prefixSums1, l - 1), getLinesCount(prefixSums2, width - l + 1))
            ).toString()
        else
            max(getLinesCount(prefixSums1, l), getLinesCount(prefixSums2, width - l)).toString()
    )
}

fun getLinesCount(prefixSums: List<Long>, width: Int): Int {
    var start = 0
    var count = 0
    while (start != prefixSums.lastIndex) {

        var r = prefixSums.size
        var l = start

        while (l < r) {
            val medium = (l + r) / 2
            if (prefixSums[medium] - prefixSums[start] - 2 >= width) {
                r = medium
            } else {
                l = medium + 1
            }
        }

        start = l - 1
        count++
    }

    return count
}