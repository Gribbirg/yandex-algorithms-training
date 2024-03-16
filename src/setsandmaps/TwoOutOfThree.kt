package setsandmaps

fun main() {
    val countMap = HashMap<Int, Int>()

    repeat(3) {
        readln()
        readln()
            .split(' ')
            .map { it.toInt() }
            .toSet()
            .forEach { countMap[it] = countMap.getOrDefault(it, 0) + 1 }
    }

    println(
        countMap
            .filter { (_, value) -> value >= 2 }
            .keys
            .sorted()
            .joinToString(" ")
    )
}