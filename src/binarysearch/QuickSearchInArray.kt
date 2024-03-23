package binarysearch

import java.io.File

fun main() {
    val input = File("input.txt").bufferedReader()
    val output = File("output.txt")
    output.writeText("")
    val n = input.readLine().toInt()
    val nums = input.readLine().split(' ').map { it.toInt() }.sorted()
    val k = input.readLine().toInt()

    val res = StringBuilder()

    repeat(k) {
        val (left, right) = input.readLine().split(' ').map { it.toInt() }
        var add = -1

        var indexLeft = nums.binarySearch(left)
        if (indexLeft < 0) {
            indexLeft = -indexLeft - 1
            add = 0
        } else {
            indexLeft = firstBinSearch(0, indexLeft, nums, left) - 1
        }

        var indexRight = nums.binarySearch(right)
        if (indexRight < 0) {
            indexRight = -indexRight - 1
        } else {
            indexRight = lastBinSearch(indexRight, nums.lastIndex, nums, right) + 1
        }

        res.append("${indexRight - indexLeft + add} ")
    }
    output.appendText(res.toString())
}

fun lastBinSearch(left: Int, right: Int, nums: List<Int>, searched: Int): Int {
    var l = left
    var r =  right
    while (l < r) {
        val medium = (l + r + 1) / 2
        if (nums[medium] == searched) {
            l = medium
        } else {
            r = medium - 1
        }
    }
    return l
}

fun firstBinSearch(left: Int, right: Int, nums: List<Int>, searched: Int): Int {
    var l = left
    var r =  right
    while (l < r) {
        val medium = (l + r) / 2
        if (nums[medium] == searched) {
            r = medium
        } else {
            l = medium + 1
        }
    }
    return l
}