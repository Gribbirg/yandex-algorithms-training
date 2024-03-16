package setsandmaps

import kotlin.math.max

fun main() {
    val n = readln().toInt()
    val numbers = readln().split(' ').map { it.toInt() }

    val neighboursHighMap = HashMap<Int, Int>()
    val neighboursLowMap = HashMap<Int, Int>()
    val countMap = HashMap<Int, Int>()

    numbers.forEach { num ->
        neighboursHighMap[num - 1] = neighboursHighMap.getOrDefault(num - 1, 0) + 1
        neighboursLowMap[num + 1] = neighboursLowMap.getOrDefault(num + 1, 0) + 1
        countMap[num] = countMap.getOrDefault(num, 0) + 1
    }

    val max = countMap.maxOf { (key, value) ->
        value + max(neighboursLowMap.getOrDefault(key, 0), neighboursHighMap.getOrDefault(key, 0))
    }
    println(n - max)
}