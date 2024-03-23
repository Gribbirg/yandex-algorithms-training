package binarysearch

import kotlin.math.ceil
import kotlin.math.sqrt

fun main() {
    val x = readln().toLong()

    var sum = ceil(sqrt(2 * x + .25) - .5).toLong()
    var maxValueOfSum = (sum + 1) * sum / 2

    if (maxValueOfSum < x) {
        sum++
        maxValueOfSum = (sum + 1) * sum / 2
    }

    val back = maxValueOfSum - x

    if (sum % 2 == 0L)
        println("${back + 1}/${sum - back}")
    else
        println("${sum - back}/${back + 1}")
}