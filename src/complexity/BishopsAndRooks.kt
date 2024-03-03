package complexity

fun main() {
    val matrix = List(8) { readln().toCharArray().toList().subList(0, 8).toCharArray() }

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            if (matrix[i][j] == 'R') {
                var y = i + 1
                var x = j
                while (y < 8 && (matrix[y][x] == '*' || matrix[y][x] == '+')) {
                    matrix[y][x] = '+'
                    y++
                }

                y = i - 1
                x = j
                while (y >= 0 && (matrix[y][x] == '*' || matrix[y][x] == '+')) {
                    matrix[y][x] = '+'
                    y--
                }

                y = i
                x = j + 1
                while (x < 8 && (matrix[y][x] == '*' || matrix[y][x] == '+')) {
                    matrix[y][x] = '+'
                    x++
                }

                y = i
                x = j - 1
                while (x >= 0 && (matrix[y][x] == '*' || matrix[y][x] == '+')) {
                    matrix[y][x] = '+'
                    x--
                }
            } else if (matrix[i][j] == 'B') {
                var y = i + 1
                var x = j + 1
                while (y < 8 && x < 8 && (matrix[y][x] == '*' || matrix[y][x] == '+')) {
                    matrix[y][x] = '+'
                    x++
                    y++
                }

                y = i + 1
                x = j - 1
                while (y < 8 && x >= 0 && (matrix[y][x] == '*' || matrix[y][x] == '+')) {
                    matrix[y][x] = '+'
                    x--
                    y++
                }

                y = i - 1
                x = j + 1
                while (y >= 0 && x < 8 && (matrix[y][x] == '*' || matrix[y][x] == '+')) {
                    matrix[y][x] = '+'
                    x++
                    y--
                }

                y = i - 1
                x = j - 1
                while (y >= 0 && x >= 0 && (matrix[y][x] == '*' || matrix[y][x] == '+')) {
                    matrix[y][x] = '+'
                    x--
                    y--
                }
            }
        }
    }

    var count = 0
    matrix.forEach { count += it.count { ch -> ch == '*'} }
    println(count)
}