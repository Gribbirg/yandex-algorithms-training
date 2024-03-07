package linearsearch

import kotlin.math.max

fun main() {
    val n = readln().toInt()
    val sectors = readln().split(' ').map { it.toInt() }
    val (minSpeed, maxSpeed, slowDown) = readln().split(' ').map { it.toInt() }

    val minDistance = (minSpeed - 1) / slowDown
    val maxDistance = (maxSpeed - 1) / slowDown

    if (maxDistance - minDistance >= n) {
        println(sectors.max())
        return
    }

    var max = 0

    for (i in minDistance..maxDistance) {
        max = max(max, sectors[i % n])
        max = max(max, sectors[(n - (i % n)) % n])
    }

    println(max)
}