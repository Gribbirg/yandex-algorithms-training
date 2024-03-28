package binarysearch

import java.io.File
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class LaptaField(
    private val maxDistance: Double,
    private val players: List<AbstractPlayer>
) {
    private val playersWithAdd: List<AbstractPlayer> = players
        .toMutableList()
        .also { list ->
            list.add(
                OneSpeedPlayer(
                    .0,
                    .0,
                    maxDistance
                )
            )
        }

    companion object {
        private const val MATH_ERROR = 0.00001
    }

    open class Point(
        val x: Double,
        val y: Double
    ) {
        fun getDistanceTo(other: Point) =
            sqrt((x - other.x).pow(2) + (y - other.y).pow(2))

        override fun toString(): String {
            return "Point(x=$x, y=$y)"
        }

        open operator fun plus(other: Point) =
            Point(x + other.x, y + other.y)

        open operator fun minus(other: Point) =
            Point(x - other.x, y - other.y)

        open operator fun unaryMinus() =
            Point(-x, -y)

        operator fun times(a: Double) =
            Point(x * a, y * a)
    }

    abstract class AbstractPlayer(
        x: Double,
        y: Double,
        val speed: Double
    ) : Point(x, y) {

        override operator fun plus(other: Point) =
            Player(x + other.x, y + other.y, speed)

        override operator fun minus(other: Point) =
            Player(x - other.x, y - other.y, speed)

        override operator fun unaryMinus() =
            Player(-x, -y, speed)

        abstract fun getRadius(time: Double): Double

        fun findIntersections(other: AbstractPlayer, time: Double): List<Point> {


            val distance = getDistanceTo(other)
            val radius = getRadius(time)
            val otherRadius = other.getRadius(time)

            if (distance > radius + otherRadius || distance < abs(radius - otherRadius)) {

                return listOf()

            } else if (distance == radius + otherRadius || distance == abs(radius - otherRadius)) {

                val k = speed / distance
                return listOf(
                    Point(
                        other.x * k,
                        other.y * k
                    )
                )

            } else {

                val a = (radius.pow(2) - otherRadius.pow(2) + distance.pow(2)) / (2 * distance)
                val h = sqrt(radius.pow(2) - a.pow(2))
                val centerPoint: Point = this + (other - this) * (a / distance)

                return listOf(
                    Point(
                        centerPoint.x + (h / distance) * (other.y - y),
                        centerPoint.y - (h / distance) * (other.x - x)
                    ),
                    Point(
                        centerPoint.x - (h / distance) * (other.y - y),
                        centerPoint.y + (h / distance) * (other.x - x)
                    )
                )
//                    .also { println(it) }

            }

        }


        override fun toString(): String {
            return "Player(center=${super.toString()},speed=$speed)"
        }
    }

    open class Player(
        x: Double,
        y: Double,
        speed: Double
    ) : AbstractPlayer(x, y, speed) {
        constructor(list: List<Double>) : this(list[0], list[1], list[2])

        override fun getRadius(time: Double) = speed * time
    }

    class OneSpeedPlayer(x: Double, y: Double, speed: Double) : AbstractPlayer(x, y, speed) {
        override fun getRadius(time: Double) =
            speed
    }

    data class FindBestPointResult(
        val point: Point,
        val time: Double
    ) {
        override fun toString(): String {
            return "$time\n${point.x} ${point.y}"
        }
    }

    fun findBestPoint(): FindBestPointResult {

        var resPoint: Point? = null

        fun check(time: Double): Boolean {

            val intersectionPoints = mutableListOf(Point(.0, maxDistance))

            (0..<playersWithAdd.lastIndex).forEach { i ->
                (i + 1..playersWithAdd.lastIndex).forEach { j ->
                    val player1 = playersWithAdd[i]
                    val player2 = playersWithAdd[j]
                    intersectionPoints.addAll(player1.findIntersections(player2, time))
                }
            }

            val resPoints = intersectionPoints.filterNot { point ->
//                println(point)
//                println(point.getDistanceTo(Point(.0, .0)) > maxDistance + MATH_ERROR)
//                println(point.getDistanceTo(Point(.0, .0)))
//                println(maxDistance + MATH_ERROR)
//                println(players.any { player ->
//                    (player.getDistanceTo(point) + MATH_ERROR < player.getRadius(time)).also { if (it) {
//                        println(player)
//                        println(player.getDistanceTo(point))
//                        println(player.getRadius(time))
//                    }
//                    }
//                })
//                println()
                point.y < 0 ||
                        point.getDistanceTo(Point(.0, .0)) > maxDistance + MATH_ERROR ||
                        players.any { player ->
                            player.getDistanceTo(point) + MATH_ERROR < player.getRadius(time)
                        }

            }

            if (resPoints.isEmpty()) {
                return false
            } else {
                resPoint = resPoints.first()
                return true
            }
        }

        var l = .0
        var r = maxDistance * players.maxOf { it.speed }

        while (l + MATH_ERROR < r) {
            val medium = (l + r) / 2
            if (check(medium)) {
                l = medium
            } else {
                r = medium
            }
        }

        return FindBestPointResult(
            resPoint!!,
            l
        )
    }
}

fun main() {
    val input = File("input.txt").bufferedReader()

    val (maxDistance, n) = input.readLine().split(" ").map { it.toInt() }
    val players = List(n) {
        LaptaField.Player(input.readLine().split(" ").map { it.toDouble() })
    }

    val field = LaptaField(maxDistance.toDouble(), players)

    println(field.findBestPoint())
}