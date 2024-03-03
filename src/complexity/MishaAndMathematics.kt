package complexity

fun main() {
    val n = readln().toInt()
    val nums = readln().split(' ').map { it.toInt() }
    val res = MutableList(n - 1) { 'x' }



    println(res.joinToString(""))
}