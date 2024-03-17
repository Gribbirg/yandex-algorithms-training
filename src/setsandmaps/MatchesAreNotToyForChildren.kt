package setsandmaps

import java.io.File

data class Match(
    val start: Point,
    val end: Point
) : Comparable<Match> {
    val dx = start.x - end.x
    val dy = start.y - end.y

    data class Point(
        val x: Int,
        val y: Int
    ) : Comparable<Point> {
        override fun compareTo(other: Point): Int =
            if (x != other.x) x - other.x
            else y - other.y

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Point

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

    }

    companion object {
        fun fromLine(line: String): Match {
            val coords = line.split(' ').map { it.toInt() }
            val point1 = Point(coords[0], coords[1])
            val point2 = Point(coords[2], coords[3])
            return Match(minOf(point1, point2), maxOf(point1, point2))
        }
    }

    override fun compareTo(other: Match): Int =
        if (start != other.start) start.compareTo(other.start)
        else end.compareTo(other.end)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Match

        if (start != other.start) return false
        if (end != other.end) return false
        if (dx != other.dx) return false
        if (dy != other.dy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + dx
        result = 31 * result + dy
        return result
    }
}

fun main() {
    val input = File("input.txt").bufferedReader()
    val n = input.readLine().toInt()
    val matches = List(n) {
        Match.fromLine(input.readLine())
    }
    val matchesTarget = List(n) {
        Match.fromLine(input.readLine())
    }

    val map = HashMap<Match.Point, Int>()

    matches.forEach { match ->
        matchesTarget.forEach { target ->
            if (target.dx == match.dx && target.dy == match.dy) {
                val x = target.start.x - match.start.x
                val y = target.start.y - match.start.y
                val point = Match.Point(x, y)
                map[point] = map.getOrDefault(point, 0) + 1
            }
        }
    }

    println(n - if (map.isNotEmpty()) map.values.max() else 0)
}