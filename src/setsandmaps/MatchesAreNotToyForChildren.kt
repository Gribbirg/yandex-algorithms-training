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
            Point(start.x - x, start.y - y),
            Point(end.x - x, end.y - y)
        )

    data class Point(
        val x: Int,
        val y: Int
    ) : Comparable<Point> {
        override fun compareTo(other: Point): Int =
            if (x != other.x) x - other.x
            else y - other.y

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
    val matches = HashSet<Match>(n)
    val matchesTarget = HashSet<Match>(n)
    repeat(n) {
        matches.add(Match.fromLine(input.readLine()))
    }
    repeat(n) {
        matchesTarget.add(Match.fromLine(input.readLine()))
    }

    var res = 0

    for (x in -100..100) {
        for (y in -100..100) {
            res = max(
                res,
                matches
                    .map { it.movedBy(x, y) }
                    .intersect(matchesTarget)
                    .size
            )
        }
    }

    println(n - res)
}