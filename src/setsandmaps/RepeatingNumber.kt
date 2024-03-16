package setsandmaps

import java.util.LinkedList
import java.util.Queue

enum class RepeatingNumberAnswers {
    NO,
    YES
}

fun main() {
    val (_, delta) = readln().split(' ').map { it.toInt() }
    val nums = readln().split(' ').filterNot { it == "" }.map { it.toInt() }

    val values = HashSet<Int>()
    val numsQueue: Queue<Int> = LinkedList()

    nums.forEachIndexed { index, num ->
        if (values.contains(num)) {
            println(RepeatingNumberAnswers.YES)
            return
        }

        if (index - delta >= 0) {
            val removingValue = numsQueue.poll()
            values.remove(removingValue)
        }

        numsQueue.add(num)
        values.add(num)
    }

    println(RepeatingNumberAnswers.NO)
}