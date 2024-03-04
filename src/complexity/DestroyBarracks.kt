package complexity

import kotlin.math.min

fun main() {
    val soldiers = readln().toInt()
    var hp = readln().toInt() - soldiers
    val enemiesAdd = readln().toInt()
    var steps = 1
    var sol = Int.MAX_VALUE

    if (hp <= 0) {
        println(1)
        return
    }

    if (soldiers > enemiesAdd) {
        while (soldiers < hp) {
            hp -= (soldiers - enemiesAdd)
            steps++
        }
        while (enemiesAdd - (soldiers - hp) > soldiers - (enemiesAdd - (soldiers - hp))) {
            hp -= (soldiers - enemiesAdd)
            steps++

            exitTry(enemiesAdd, soldiers, hp).let {
                if (it != -1) {
                    sol = min(sol, steps + it)
                }
            }
        }
        println(min(sol, steps + if (enemiesAdd + hp <= soldiers) 1 else 2))
    } else {
        exitTry(enemiesAdd, soldiers, hp).let { if (it != -1) println(it + 1) else println(-1) }
    }
}

fun exitTry(enemiesAdd: Int, soldiersIn: Int, hpIn: Int): Int {
    var soldiers = soldiersIn
    var hp = hpIn
    var steps = 1
    var enemies = enemiesAdd

    enemies -= (soldiers - hp)
    hp -= soldiers
    soldiers -= if (enemies > 0) enemies else 0
    while (enemies > 0) {
        enemies -= soldiers
        soldiers -= if (enemies > 0) enemies else 0
        steps++
        if (hp > 0 || soldiers <= 0) {
            return -1
        }
    }
    return steps
}