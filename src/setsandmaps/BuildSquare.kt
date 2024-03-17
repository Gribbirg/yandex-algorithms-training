package setsandmaps

import java.io.File

data class BuildSquarePoint(
    val x: Int,
    val y: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BuildSquarePoint

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "$x $y"
    }

    companion object {
        fun fromLine(line: String): BuildSquarePoint {
            line.split(' ').map { it.toInt() }.let {
                return BuildSquarePoint(it[0], it[1])
            }
        }

        fun getAllCandidates(point1: BuildSquarePoint, point2: BuildSquarePoint): List<List<BuildSquarePoint>> {
            val dx = point1.x - point2.x
            val dy = point1.y - point2.y

            return listOf(
                listOf(
                    BuildSquarePoint(point1.x - dy, point1.y + dx),
                    BuildSquarePoint(point2.x - dy, point2.y + dx)
                ),
                listOf(
                    BuildSquarePoint(point1.x + dy, point1.y - dx),
                    BuildSquarePoint(point2.x + dy, point2.y - dx)
                )
            )
        }
    }
}

fun main() {
    val input = File("input.txt").bufferedReader()
    val n = input.readLine().toInt()
    val points = List(n) {
        BuildSquarePoint.fromLine(input.readLine())
    }
    val pointsSet = points.toSet()

    if (points.size == 1) {
        val point = points.first()
        println(3)
        println("${point.x} 0")
        println("0 ${point.y}")
        println("0 0")
        return
    }

    var ans = 4
    var ansPoints = listOf(
        BuildSquarePoint(0, 0),
        BuildSquarePoint(1, 0),
        BuildSquarePoint(0, 1),
        BuildSquarePoint(1, 1)
    )

    for (i in 0..n - 2) {
        for (j in i + 1..<n) {
            val point1 = points[i]
            val point2 = points[j]

            val candidates = BuildSquarePoint.getAllCandidates(point1, point2)
            candidates.forEach { line ->
                val isCandidate1 = pointsSet.contains(line[0])
                val isCandidate2 = pointsSet.contains(line[1])

                if (isCandidate1 && isCandidate2) {
                    println(0)
                    return
                }

                if (isCandidate1) {
                    ans = 1
                    ansPoints = listOf(line[1])
                }
                if (isCandidate2) {
                    ans = 1
                    ansPoints = listOf(line[0])
                }

                if (ans > 2) {
                    ans = 2
                    ansPoints = line
                }
            }
        }
    }
    println(ans)
    ansPoints.forEach {
        println(it)
    }
}