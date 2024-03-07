package linearsearch

import java.io.File
import kotlin.math.abs
import kotlin.math.min

data class Ship(
    val y: Int,
    val x: Int
) {
    constructor(coords: List<Int>) : this(coords[0], coords[1])
}

fun main() {
    val input = File("input.txt").bufferedReader()
    val n = input.readLine().toInt()
    val ships = ArrayList<Ship>()
    repeat(n) {
        ships.add(Ship(input.readLine().split(' ').map { it.toInt() - 1 }))
    }

    var minSteps = Int.MAX_VALUE
    for (target in 0..<n) {
        var steps = 0
        ships.sortedWith { ship1, ship2 ->
            when {
                ship1.y != ship2.y -> ship1.y - ship2.y
                else -> abs(target - ship1.x) - abs(target - ship2.x)
            }
        }.forEachIndexed { index, ship ->
            steps += abs(target - ship.x) + abs(index - ship.y)
        }
        minSteps = min(minSteps, steps)
    }

    println(minSteps)
}