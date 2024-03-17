package setsandmaps

import java.io.File
import kotlin.math.max

data class Match(
    val start: Point,
    val end: Point
) : Comparable<Match> {
    val dx = start.x - end.x
    val dy = start.y - end.y

    fun movedBy(x: Int, y: Int) =
        Match(
            Point(start.x + x, start.y + y),
            Point(end.x + x, end.y + y)
        )

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
}

fun main() {
    val input = File("input.txt").bufferedReader()
    val n = input.readLine().toInt()
    val matches = ArrayList<Match>(n)
    val matchesTarget = ArrayList<Match>(n)
    repeat(n) {
        matches.add(Match.fromLine(input.readLine()))
    }
    repeat(n) {
        matchesTarget.add(Match.fromLine(input.readLine()))
    }

    matches.sort()
    matchesTarget.sort()

    var res = 0
    val blackList = HashSet<Match.Point>()

    matches.forEach { match ->
        matchesTarget.filter { it.dx == match.dx && it.dy == match.dy }.forEach { target ->
            val x = target.start.x - match.start.x
            val y = target.start.y - match.start.y
            val point = Match.Point(x, y)
            if (!blackList.contains(point)) {
                var attemptRes = 0
                for (i in matches.indices) {
                    if (matches[i].movedBy(x, y).compareTo(matchesTarget[i]) == 0)
                        attemptRes++
                }
                res = max(res, attemptRes)
                blackList.add(point)
            }
        }
    }

    println(res)
}