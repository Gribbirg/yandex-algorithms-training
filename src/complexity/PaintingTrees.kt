package complexity

fun main() {
    val (bucket1, radius1) = readln().split(' ').map { s: String -> s.toInt() }
    val (bucket2, radius2) = readln().split(' ').map { s: String -> s.toInt() }

    val start1 = bucket1 - radius1
    val end1 = bucket1 + radius1

    val start2 = bucket2 - radius2
    val end2 = bucket2 + radius2

    val (startMin, endOfMin, startMax, endOfMax) =
        if (start1 <= start2) intArrayOf(start1, end1, start2, end2)
        else intArrayOf(start2, end2, start1, end1)

    if (endOfMin < startMax) {
        println(radius1 * 2 + radius2 * 2 + 2)
    } else if (endOfMin < endOfMax) {
        println(radius1 * 2 + radius2 * 2 + 1 - (endOfMin - startMax))
    } else {
        println(endOfMin - startMin + 1)
    }
}