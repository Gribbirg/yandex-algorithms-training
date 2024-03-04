package complexity

import kotlin.math.abs
import kotlin.math.min

fun main() {
    var (length, x1, v1, x2, v2) = readln().split(' ').map { it.toDouble() }

    if (x1 == x2) {
        println("YES")
        println(.0)
        return
    }

    if (v1 == v2 && v1 == .0) {
        println("NO")
        return

    }
    println("YES")

    if (v1 <= 0 && v2 <= 0) {
        v1 = -v1
        v2 = -v2
        x1 = (length - x1) % length
        x2 = (length - x2) % length
    }


    val onePointTime = abs((if (v2 - v1 > 0) (x1 - x2 + length) % length else (x2 - x1 + length) % length) / (v2 - v1))

    val centerSpeed =
        if (v1 > 0 && v2 > 0) abs(min(abs(v1),abs(v2)) + abs((v2 - v1) / 2))
        else abs(abs(v2) - abs(v1)) / 2
    val centerStart = (x1 + x2) / 2
    val centerToCenterTime =
        if (length / 2 >= centerStart) (length / 2 - centerStart) / centerSpeed
        else (length - centerStart) / centerSpeed

    println(min(onePointTime, centerToCenterTime))
}