package binarysearch

import java.io.File
import kotlin.math.abs

class RainField(
    points: List<Point>,
    height: Double
) {
    private val rainArea = (points.last().x - points.first().x) * height
    private val head: Figure

    init {
        head = if (points.size == 1)
            Triangle(
                points.first().copy(y = Double.MAX_VALUE),
                points.last().copy(y = Double.MAX_VALUE),
                points.first()
            )
        else
            Trapezoid(
                points.first().copy(y = Double.MAX_VALUE),
                points.last().copy(y = Double.MAX_VALUE),
                points
            )
    }

    fun getMaxHeight() =
        head.fillArea(rainArea).height

    data class Point(
        val x: Double,
        val y: Double,
//        val id: Int = 0
    ) {
        fun getPointOnY(otherPoint: Point, y: Double): Point =
            Point(
                (y - this.y) * (otherPoint.x - this.x) / (otherPoint.y - this.y) + this.x,
                y
            )
    }

    data class AreaResult(
        var area: Double,
        var height: Double,
        var full: Boolean
    ) {
        override fun toString(): String {
            return "AreaResult(area=$area, height=$height, full=$full)"
        }
    }

    abstract class Figure {
        var filledArea: Double? = null
        var filledHeight: Double? = null
        abstract fun getArea(): Double
        abstract fun getHeight(): Double
        abstract fun getHeightFilledByArea(area: Double): Double
        abstract fun fillArea(maxArea: Double): AreaResult
    }

    class Trapezoid(
        private val pointLeftUp: Point,
        private val pointRightUp: Point,
        points: List<Point>,
        leftDrain: Point = pointLeftUp,
        rightDrain: Point = pointRightUp
    ) : Figure() {
        private val pointLeftDown: Point
        private val pointRightDown: Point
        private val child: List<Figure>
        private val rainRatio: List<Double>

        init {
            val downEdgeY = points.maxOf { it.y }
            val downEdgeIndex = points.indexOfFirst { it.y == downEdgeY }
            val downEdge = points[downEdgeIndex]

            when (downEdgeIndex) {
                0 -> {
                    pointLeftDown = downEdge
                    pointRightDown = pointRightUp.getPointOnY(points.last(), downEdge.y)
                    child = listOf(points.subList(1, points.size).let {
                        if (it.size == 1)
                            Triangle(pointLeftDown, pointRightDown, it.first())
                        else
                            Trapezoid(pointLeftDown, pointRightDown, it, leftDrain, rightDrain)
                    })
                    rainRatio = listOf(1.0)
                }

                points.lastIndex -> {
                    pointLeftDown = pointLeftUp.getPointOnY(points.first(), downEdge.y)
                    pointRightDown = downEdge
                    child = listOf(points.subList(0, points.lastIndex).let {
                        if (it.size == 1)
                            Triangle(pointLeftDown, pointRightDown, it.first())
                        else
                            Trapezoid(pointLeftDown, pointRightDown, it, leftDrain, rightDrain)
                    })
                    rainRatio = listOf(1.0)
                }

                else -> {
                    pointLeftDown = pointLeftUp.getPointOnY(points.first(), downEdge.y)
                    pointRightDown = pointRightUp.getPointOnY(points.last(), downEdge.y)
                    child = listOf(
                        points.subList(0, downEdgeIndex).let {
                            if (it.size == 1)
                                Triangle(pointLeftDown, downEdge, it.first())
                            else
                                Trapezoid(pointLeftDown, downEdge, it, leftDrain, downEdge)
                        },
                        points.subList(downEdgeIndex + 1, points.size).let {
                            if (it.size == 1)
                                Triangle(downEdge, pointRightDown, it.first())
                            else
                                Trapezoid(downEdge, pointRightDown, it, downEdge, rightDrain)
                        }
                    )
                    val firstRatio = (downEdge.x - leftDrain.x) / (rightDrain.x - leftDrain.x)
                    rainRatio = listOf(
                        firstRatio,
                        1.0 - firstRatio
                    )
                }
            }
        }

        override fun getArea(): Double =
            (pointRightDown.x - pointLeftDown.x + pointRightUp.x - pointLeftUp.x) * getHeight() / 2.0


        override fun fillArea(maxArea: Double): AreaResult {
            val childArea = mutableListOf<AreaResult>()

            if (child.size == 1) {
                childArea.add(
                    if (child.first().filledArea != null)
                        AreaResult(maxArea - child.first().filledArea!!, child.first().filledHeight!!, true)
                    else
                        child.first().fillArea(maxArea)
                )
            } else {

                childArea.addAll(
                    when (child.count { it.filledArea != null }) {
                        0 -> child.mapIndexed { index, childItem ->
                            childItem.fillArea(maxArea * rainRatio[index])
                        }

                        1 -> {
                            val filledIndex = if (child[0].filledArea != null) 0 else 1
                            val notFilledIndex = if (filledIndex == 0) 1 else 0
                            val filled = AreaResult(.0, child[filledIndex].getHeight(), true)
                            logPut.appendLine("Reload:")
                            logPut.appendLine("From: $this")
                            logPut.appendLine("To: ${child[notFilledIndex]}")
                            logPut.appendLine("")
                            val notFilled = child[notFilledIndex].fillArea(
                                maxArea - child[filledIndex].filledArea!!
                            )

                            if (filledIndex == 0)
                                listOf(filled, notFilled)
                            else
                                listOf(notFilled, filled)
                        }

                        else -> child.mapIndexed { index, childItem ->
                            AreaResult(maxArea * rainRatio[index] - childItem.filledArea!!, childItem.filledHeight!!, true)
                        }
                    }
                )


                if (childArea[0].area == .0 && childArea[1].area > .0) {
                        logPut.appendLine("Reload:")
                        logPut.appendLine("From: $this")
                        logPut.appendLine("To: ${childArea[0]}")
                        logPut.appendLine("")
                    childArea[0] = child[0].fillArea(maxArea - child[1].filledArea!!)
                    childArea[1].area = .0
                } else if (childArea[1].area == .0 && childArea[0].area > .0) {
                        logPut.appendLine("Reload:")
                        logPut.appendLine("From: $this")
                        logPut.appendLine("To: ${childArea[1]}")
                        logPut.appendLine("")
                    childArea[1] = child[1].fillArea(maxArea - child[0].filledArea!!)
                    childArea[0].area = .0
                }
            }

            val childHeight = childArea.maxOf { it.height }

            if (childArea.all
                { it.area == .0 }
            ) {
                val res = AreaResult(.0, childHeight, false)
                logPut.appendLine("Add:")
                logPut.appendLine("From: $this")
                logPut.appendLine("Result: $res")
                logPut.appendLine("Child results:")
                logPut.appendLine(childArea[0].toString())
                if (childArea.size == 2)
                    logPut.appendLine(childArea[1].toString())
                logPut.appendLine("Max area: $maxArea")
                logPut.appendLine("")
                return res
            }

            val childAreaSum = childArea.sumOf { it.area }
            val area = getArea()

            val freeArea = childAreaSum - area
            val res = if (freeArea >= .0) {
                filledArea = maxArea - freeArea
                filledHeight = childHeight + getHeight()
                AreaResult(freeArea, childHeight + getHeight(), true)
            } else {
                AreaResult(.0, childHeight + getHeightFilledByArea(childAreaSum), false)
            }
            logPut.appendLine("Add:")
            logPut.appendLine("From: $this")
            logPut.appendLine("Result: $res")
            logPut.appendLine("Child results:")
            if (childArea.size == 2)
                logPut.appendLine(childArea[1].toString())
            logPut.appendLine("Max area: $maxArea")
            logPut.appendLine("")
            return res
        }


        override fun getHeight(): Double =
            pointLeftUp.y - pointLeftDown.y

        override fun getHeightFilledByArea(area: Double): Double {
            var l = .0
            var r = getHeight()
            while (l + 0.00001 < r) {
                val medium = (l + r) / 2
                if (
                    (pointRightDown.getPointOnY(pointRightUp, pointRightDown.y + medium).x -
                            pointLeftDown.getPointOnY(pointLeftUp, pointLeftDown.y + medium).x +
                            pointRightDown.x - pointLeftDown.x) * medium / 2.0 >= area
                ) {
                    r = medium
                } else {
                    l = medium
                }
            }

            return l
        }

        override fun toString(): String {
            return "Trapezoid(pointLeftUp=$pointLeftUp, pointRightUp=$pointRightUp, pointLeftDown=$pointLeftDown, pointRightDown=$pointRightDown, child=${child.size}, rainRatio=$rainRatio)"
        }
    }

    class Triangle(
        private val pointLeftUp: Point,
        private val pointRightUp: Point,
        private val pointDown: Point
    ) : Figure() {
        override fun getArea(): Double =
            abs(
                (pointRightUp.x - pointLeftUp.x) * (pointDown.y - pointLeftUp.y) -
                        (pointDown.x - pointLeftUp.x) * (pointRightUp.y - pointLeftUp.y)
            ) / 2.0

        override fun getHeight(): Double =
            pointLeftUp.y - pointDown.y

        override fun getHeightFilledByArea(area: Double): Double {
            var l = .0
            var r = getHeight()
            while (l + 0.00001 < r) {
                val medium = (l + r) / 2
                val mediumPointRightUp = pointDown.getPointOnY(pointRightUp, pointDown.y + medium)
                val mediumPointLeftUp = pointDown.getPointOnY(pointLeftUp, pointDown.y + medium)
                if (
                    abs(
                        (mediumPointRightUp.x - mediumPointLeftUp.x) * (pointDown.y - mediumPointLeftUp.y) -
                                (pointDown.x - mediumPointLeftUp.x) * (mediumPointRightUp.y - mediumPointLeftUp.y)
                    ) / 2.0 >= area
                ) {
                    r = medium
                } else {
                    l = medium
                }
            }
            return l
        }

        override fun fillArea(maxArea: Double): AreaResult {
            val area = getArea()
            val res = if (area <= maxArea) {
                filledArea = area
                filledHeight = getHeight()
                AreaResult(maxArea - area, getHeight(), true)
            } else {
                AreaResult(.0, getHeightFilledByArea(maxArea), false)
            }
            logPut.appendLine("Triangle:")
            logPut.appendLine("From: $this")
            logPut.appendLine("Result: $res")
            logPut.appendLine("Points:")
            logPut.appendLine(pointDown.getPointOnY(pointRightUp, pointDown.y + res.height).toString())
            logPut.appendLine(pointDown.getPointOnY(pointLeftUp, pointDown.y + res.height).toString())
            logPut.appendLine("Area: ${if (filledArea != null) filledArea else maxArea}")
            logPut.appendLine("")
            return res
        }

        override fun toString(): String {
            return "Triangle(pointLeftUp=$pointLeftUp, pointRightUp=$pointRightUp, pointDown=$pointDown)"
        }
    }
}

val logPut = File("log.txt").bufferedWriter().also { it.write("") }

fun main() {
    val input = File("input.txt").bufferedReader()
    val (n, h) = input.readLine().split(" ")
    val points = List(n.toInt() + 1) {
        input.readLine().split(" ").filterNot { it.isEmpty() }.map { it.toDouble() }.let { cords ->
            RainField.Point(cords[0], cords[1])
        }
    }

    val field = RainField(points, h.toDouble())
    println(field.getMaxHeight())
}