package linearsearch

fun main() {
    val n = readln().toInt()
    val sizes = readln().split(' ').map { it.toInt() }

    val max = sizes.max()
    val sum = sizes.sum()
    val dif = sum - max

    if (max > dif)
        println(max - dif)
    else
        println(sum)
}