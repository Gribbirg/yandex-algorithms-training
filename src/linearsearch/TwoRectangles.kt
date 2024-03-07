package linearsearch

import java.io.File

fun main() {
    val input = File("input.txt").bufferedReader()
    val (linesCount, columnsCount) = input.readLine().split(' ').map { it.toInt() }
    val filed = List(linesCount) { input.readLine().toCharArray() }

    var aPlaced = false
    var aLine = -1
    var aRow = -1
    var bPlaced = false
    filed.forEachIndexed { lineIndex, line ->
        for (i in line.indices) {
            if (line[i] == '#') {

                if (aPlaced && bPlaced) {
                    println("NO")
                    return
                }

                if (!aPlaced) {
                    aLine = lineIndex
                    aRow = i
                }

                var iPlaced = i
                val sym = if (aPlaced) 'b' else 'a'

                do {
                    filed[lineIndex][iPlaced] = sym
                    iPlaced++
                } while (iPlaced <= line.lastIndex && line[iPlaced] == '#')
                iPlaced--

                var jPlaced = lineIndex + 1

                while (
                    jPlaced <= filed.lastIndex &&
                    filed[jPlaced]
                        .slice(i..iPlaced).all { it == '#' } &&
                    !(i > 0 && iPlaced < filed[jPlaced].lastIndex &&
                            filed[jPlaced]
                                .slice(i - 1..iPlaced + 1).all { it == '#' })
                ) {
                    for (iNew in i..iPlaced) {
                        filed[jPlaced][iNew] = sym
                    }
                    jPlaced++
                }

                if (aPlaced)
                    bPlaced = true
                else
                    aPlaced = true
            }
        }
    }

    if (!bPlaced) {

        if (!aPlaced) {
            println("NO")
            return
        }

        val isNextSymA = aLine != filed.lastIndex && filed[aLine + 1][aRow] == 'a'
        val isNextLineA = aRow != filed[aLine].lastIndex && filed[aLine][aRow + 1] =='a'
        when {
            isNextSymA && isNextLineA -> {
                var i = aRow
                do {
                    filed[aLine][i] = 'b'
                    i++
                } while (i <= filed[aLine].lastIndex && filed[aLine][i] == 'a')
            }
            isNextSymA || isNextLineA -> {
                filed[aLine][aRow] = 'b'
            }
            else -> {
                println("NO")
                return
            }
        }
    }

    println("YES")
    filed.forEach {
        println(it.joinToString(""))
    }
}