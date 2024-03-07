package linearsearch

import kotlin.math.min

fun main() {
    val t = readln().toInt()

    repeat(t) {

        val n = readln().toInt()
        val nums = readln().split(' ').map { it.toInt() }

        val res = StringBuilder()
        var resCount = 0
        var min = Int.MAX_VALUE
        var k = 0

        for (num in nums) {
            min = min(min, num)
            k++
            if (min < k) {
                res.append(k - 1)
                res.append(" ")
                resCount++
                min = num
                k = 1
            }
        }
        res.append(k)

        println(resCount + 1)
        println(res)
    }
}