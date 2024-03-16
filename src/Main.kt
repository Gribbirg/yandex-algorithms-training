import java.io.File

fun main() {
    File("input.txt").readLines().forEach {
        println(it.split(' ').size)
    }
}