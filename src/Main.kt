import java.io.File

fun main() {
    val list = listOf(0, 0, 1, 2, 3, 3, 4)
    println(list.minBy { list.count { el -> el == it } })
}