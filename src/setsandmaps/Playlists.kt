package setsandmaps

import java.io.File

fun main() {
    val input = File("input.txt").bufferedReader()
    val fileOutput = File("output.txt")
    fileOutput.writeText("")

    val n = input.readLine().toInt()
    input.readLine()
    var set = input.readLine().split(' ').toSet()

    repeat(n - 1) {
        input.readLine()
        set = set.intersect(input.readLine().split(' ').toSet())
    }

    fileOutput.appendText(set.size.toString() + '\n' + set.sorted().joinToString(" "))
}

//import java.io.File
//
//fun main() {
//    val input = File("input.txt").bufferedReader()
//    val fileOutput = File("output.txt")
//    fileOutput.writeText("")
//
//    val n = input.readLine().toInt()
//    val map = HashMap<String, Int>()
//
//    repeat(n) {
//        input.readLine()
//        input.readLine().split(' ').forEach { map[it] = map.getOrDefault(it, 0) + 1 }
//    }
//
//    val res = map.filter { (_, value) -> value == n }.keys.sorted()
//    fileOutput.appendText(res.size.toString() + '\n' + res.joinToString(" "))
//}