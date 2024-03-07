package linearsearch

fun main() {
    val n = readln().toInt()

    val field = List(10) { BooleanArray(10) { false } }
    val dx = intArrayOf(0, 1, 0, -1)
    val dy = intArrayOf(-1, 0, 1, 0)
    var p = 0

    repeat(n) {
        val (x, y) = readln().split(' ').map { it.toInt() }

        var plusCount = 4
        for (i in 0..3)
            if (field[y + dy[i]][x + dx[i]])
                plusCount -= 2
        p += plusCount

        field[y][x] = true
    }

    println(p)
}