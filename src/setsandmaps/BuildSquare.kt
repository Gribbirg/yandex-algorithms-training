package setsandmaps

import java.io.File
import kotlin.math.abs
import kotlin.math.sqrt

data class BuildSquarePoint(
    val x: Int,
    val y: Int
) {

    fun getDistanceTo(other: BuildSquarePoint): Double {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt((dx * dx + dy * dy).toDouble())
    }

    companion object {
        fun fromLine(line: String): BuildSquarePoint {
            line.split(' ').map { it.toInt() }.let {
                return BuildSquarePoint(it[0], it[1])
            }
        }
    }
}

fun main() {
    val input = File("input.txt").bufferedReader()
    val fileOutput = File("output.txt")
    val n = input.readLine().toInt()
    val points = List(n) {
        BuildSquarePoint.fromLine(input.readLine())
    }
    var ans = 4
    var ansPoints = listOf(
        BuildSquarePoint(0, 0),
        BuildSquarePoint(1, 0),
        BuildSquarePoint(0, 1),
        BuildSquarePoint(1, 1)
    )

    for (i in 0..n - 3) {
        for (j in i + 1..n - 2) {
            for (z in j + 1..<n) {
                if (points[z].getDistanceTo(points[i]) == points[z].getDistanceTo(points[j])) {
                    ans = 1

                }
            }
        }
    }
}