package complexity

import kotlin.math.abs

fun main() {
    val n = readln().toInt()
    val nums = readln().split(' ').map { it.toInt() }
    val res = MutableList(n - 1) { 'x' }

    var hasOdd = false
    var isNotOdd: Boolean? = null
    for (i in 0..n - 2) {
        val first = nums[i]
        val second = nums[i + 1]

        if (hasOdd && isNotOdd == null)
            isNotOdd = false

        if (hasOdd && !isNotOdd!! && first % 2 == 0)
            isNotOdd = true

        if (!hasOdd) {
            if (abs(first) % 2 == 1) {
                res[i] = '+'
                hasOdd = true
            } else if (abs(second) % 2 == 1) {
                res[i] = '+'
            }
        }
    }

    val last = nums.last()
    if (hasOdd && isNotOdd != null && !isNotOdd && abs(last) % 2 == 1 || !hasOdd && abs(last) % 2 == 1) {
        res[res.lastIndex] = '+'
    } else if (abs(last) % 2 == 1) {
        res[res.lastIndex] = 'x'
    }

    println(res.joinToString(""))
}