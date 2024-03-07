package linearsearch

import kotlin.math.max
import kotlin.math.min

fun main() {
    val (n, k) = readln().split(' ').map { it.toInt() }
    val price = readln().split(' ').map { it.toInt() }

    var maxProfit = 0

    for (i in 0..n - 2) {
        for (j in i..min(i + k, n - 1)) {
            maxProfit = max(maxProfit, price[j] - price[i])
        }
    }

    println(maxProfit)
}