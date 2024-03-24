package binarysearch

import java.io.File
import kotlin.math.min

data class CyclePathsPoint(
    val x: Int,
    val y: Int
): Comparable<CyclePathsPoint> {
    override fun compareTo(other: CyclePathsPoint): Int = when {
        x != other.x -> x - other.x
        else -> y - other.y
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}

fun drawPoints(points: List<CyclePathsPoint>, width: Int, height: Int) {
//    val height = points.maxOf { it.y }
//    val width = points.maxOf { it.x }
    val field = List(height) { MutableList(width) { "-" } }
    points.forEach { point ->
        field[height - point.y][point.x - 1] = "#"
    }
    field.forEach { line ->
        println(line.joinToString(""))
    }
}

fun main() {
    val input = File("input.txt").bufferedReader()

    val (width, height, n) = input.readLine().split(' ').map { it.toInt() }
    val points = List(n) {
        input.readLine().split(' ').let {
            CyclePathsPoint(it[0].toInt(), it[1].toInt())
        }
    }.sorted()
    val testPoint = points.minBy { it.x }

//    drawPoints(points, width, height)
    println(points)
    var l = 0
    var r =  min(width, height) + 1
    while (l < r) {
        val medium = (l + r) / 2
        if (check(points, testPoint, medium)) {
            r = medium
        } else {
            l = medium + 1
        }
    }
}

fun check(points: List<CyclePathsPoint>, testPoint: CyclePathsPoint, lineWidth: Int): Boolean {
    val closedX = testPoint.x + lineWidth

    if (closedX > points.last().x) return true



    return false
}