package binarysearch

import java.io.File

fun main() {
    val input = File("input.txt").bufferedReader()
    val output = File("output.txt")

    val (n, m) = input.readLine().split(' ').map { it.toInt() }
    val nums = input.readLine().split(' ').map { it.toInt() }

    val prefixSums = MutableList(n + 1) { 0L }
    prefixSums[0] = 0
    for (i in 0..nums.lastIndex) {
        prefixSums[i + 1] = prefixSums[i] + nums[i]
    }
    val res = StringBuilder()

    repeat(m) { _ ->
        val data = input.readLine().split(' ')
        val count = data[0].toInt()
        val sum = data[1].toLong()

        var r = n - count
        var l = 0

        while (l < r) {
            val medium = (l + r) / 2
            if (prefixSums[medium + count] - prefixSums[medium] >= sum) {
                r = medium
            } else {
                l = medium + 1
            }
        }

        res.append("${if (prefixSums[l + count] - prefixSums[l] != sum) -1 else l + 1}\n")
    }

    output.writeText(res.toString())
}