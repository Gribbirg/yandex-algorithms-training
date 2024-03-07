package linearsearch

import kotlin.math.max
import kotlin.math.min

fun main() {
    val n = readln().toInt()

    val (xSt, ySt) = readln().split(' ').map { it.toInt() }
    var left = xSt
    var right = xSt
    var top = ySt
    var bottom = ySt

    repeat(n - 1) {
        val (x, y) = readln().split(' ').map { it.toInt() }
        left = min(x, left)
        right = max(x, right)
        top = min(y, top)
        bottom = max(y, bottom)
    }

    println("$left $top $right $bottom")
}