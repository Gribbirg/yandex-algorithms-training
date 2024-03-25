package binarysearch

import java.io.File
import kotlin.math.max
import kotlin.math.min

data class CyclePathsPoint(
    val x: Int,
    val y: Int
) : Comparable<CyclePathsPoint> {
    override fun compareTo(other: CyclePathsPoint): Int = when {
        x != other.x -> x - other.x
        else -> y - other.y
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}

data class CycleYSuf(
    var minPref: Int,
    var minPost: Int,
    var maxPref: Int,
    var maxPost: Int
) {

    constructor(y: Int) : this(y, y, y, y)

    companion object {
        fun getList(y: List<Int>, height: Int): List<CycleYSuf> {
            val res = mutableListOf<CycleYSuf>().also {
                it.add(
                    CycleYSuf(
                        minPref = height + 1,
                        minPost = 0,
                        maxPref = -1,
                        maxPost = 0
                    )
                )
                it.addAll(y.map { yItem -> CycleYSuf(yItem) })
                it.add(
                    CycleYSuf(
                        minPref = 0,
                        minPost = height + 1,
                        maxPref = 0,
                        maxPost = -1
                    )
                )
            }

            (2..<res.lastIndex).forEach { i ->
                if (res[i - 1].minPref < y[i - 1])
                    res[i].minPref = res[i - 1].minPref
                if (res[i - 1].maxPref > y[i - 1])
                    res[i].maxPref = res[i - 1].maxPref
            }

            (res.lastIndex - 2 downTo 1).forEach { i ->
                if (res[i + 1].minPost < y[i - 1])
                    res[i].minPost = res[i + 1].minPost
                if (res[i + 1].maxPost > y[i - 1])
                    res[i].maxPost = res[i + 1].maxPost
            }

            return res
        }
    }

    override fun toString(): String {
        return "CycleYSuf(minPref=$minPref, minPost=$minPost, maxPref=$maxPref, maxPost=$maxPost)"
    }
}

fun drawPoints(points: List<CyclePathsPoint>, width: Int, height: Int) {
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
    val ySuf = CycleYSuf.getList(points.map { it.y }, height)

    var l = 0
    var r = min(width, height) + 1
    while (l < r) {
        val medium = (l + r) / 2
        if (check(points, ySuf, medium)) {
            r = medium
        } else {
            l = medium + 1
        }
    }
    println(l)
}

fun check(points: List<CyclePathsPoint>, ySuf: List<CycleYSuf>, lineWidth: Int): Boolean {
    points.forEachIndexed { endIndex, endPoint ->

        val startIndex = findFirst(points, lineWidth, endIndex, endPoint)

        if (
            startIndex == 0 && endIndex == points.lastIndex ||
            max(ySuf[startIndex].maxPref, ySuf[endIndex + 2].maxPost) -
            min(ySuf[startIndex].minPref, ySuf[endIndex + 2].minPost) < lineWidth
        )
            return true
    }

    return false
}

fun findFirst(points: List<CyclePathsPoint>, lineWidth: Int, endIndex: Int, endPoint: CyclePathsPoint): Int {
    var l = 0
    var r =  endIndex + 1
    while (l < r) {
        val medium = (l + r) / 2
        if (points[medium].x > endPoint.x - lineWidth) {
            r = medium
        } else {
            l = medium + 1
        }
    }
    return l
}