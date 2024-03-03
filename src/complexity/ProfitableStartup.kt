package complexity

fun main() {
    var (profit, count, n) = readln().split(' ').map { it.toLong() }

    profit *= 10
    val rest = profit % count
    if (rest != 0L) {
        val plus = count - rest
        if (plus >= 10L) {
            println(-1)
            return
        }
        profit += plus
    }

    print(profit)
    repeat(n.toInt() - 1) { print("0") }
}