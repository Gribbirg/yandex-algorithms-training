package linearsearch

import java.io.File

fun main() {
    val input = File("input.txt").bufferedReader()
    val n = input.readLine().toInt()

    var maxHeight = 0L
    val takeIndexesStr = StringBuilder()
    val notTakeIndexesStr = StringBuilder()
    var maxButDownUp = 0
    var maxButDownUpIndex = -1
    var maxButDownUpDif = 0

    repeat(n) { j ->
        val i = j + 1
        val (up, down) = input.readLine().split(' ').map { it.toInt() }
        val dif = up - down
        if (dif > 0) {
            if (maxButDownUp < down) {

                if (maxButDownUpIndex != -1) {
                    if (maxButDownUpDif != 0) {
                        takeIndexesStr.append(maxButDownUpIndex)
                        takeIndexesStr.append(" ")
                    } else {
                        notTakeIndexesStr.append(maxButDownUpIndex)
                        notTakeIndexesStr.append(" ")
                    }
                }

                maxButDownUp = down
                maxButDownUpIndex = i
                maxButDownUpDif = down
            } else {
                takeIndexesStr.append(i)
                takeIndexesStr.append(" ")
            }
            maxHeight += dif
        } else {
            if (maxButDownUp < up) {
                if (maxButDownUpIndex != -1) {
                    if (maxButDownUpDif != 0) {
                        takeIndexesStr.append(maxButDownUpIndex)
                        takeIndexesStr.append(" ")
                    } else {
                        notTakeIndexesStr.append(maxButDownUpIndex)
                        notTakeIndexesStr.append(" ")
                    }
                }

                maxButDownUp = up
                maxButDownUpIndex = i
                maxButDownUpDif = 0
            } else {
                notTakeIndexesStr.append(i)
                notTakeIndexesStr.append(" ")
            }
        }
    }

    println(maxHeight + maxButDownUp)
    print(takeIndexesStr)
    print("${maxButDownUpIndex} ")
    print(notTakeIndexesStr)
}