package binarysearch

import java.io.File

fun main() {
    val input = File("input.txt").bufferedReader()
    val output = File("output.txt")

    val n = input.readLine().toLong()

    var sum = 0L
    var res = 0L
    var i = 2L

    while (true) {
        sum += i
        res += sum

        if (res - 1 > n)
            break

        i++
    }

    output.writeText((i - 2).toString())
}