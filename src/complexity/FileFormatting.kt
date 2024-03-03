package complexity

fun main() {
    val n = readln().toInt()
    var res = 0L

    repeat(n) {
        val count = readln().toInt()
        val rest = count % 4

        res += count / 4 + if (rest == 3) 2 else rest
    }

    println(res)
}