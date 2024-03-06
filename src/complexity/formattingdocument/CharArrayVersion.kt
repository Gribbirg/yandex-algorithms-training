package complexity.formattingdocument

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val fileInput = File("./src/complexity/formattingdocument/input.txt")
//    val fileInput = File("input.txt")
    val fileOutput = File("./src/complexity/formattingdocument/output.txt")
//    val fileOutput = File("output.txt")
    val input = fileInput.readLines()
    fileOutput.writeText("")
    val (pageWidth, lineHeight, charWidth) = input.first().split(' ').map { it.toInt() }

    var x = 0
    var y = 0
    var yAdd = 0
    var lineCurrentHeight = lineHeight
    var imageStrMap: HashMap<String, String>? = null
    var page = ArrayList<CharArray>()
    var xLast = 0
    var yLast = 0

    fun addEmptyLines(count: Int) {
        repeat(count) {
            page.add(CharArray(pageWidth) { '-' })
        }
    }

    addEmptyLines(200)
//    printPage(page)

    input.subList(1, input.size).forEach { line ->
        val elements = line.split(" ").filterNot { it == "" }
//        println(elements)

        if (line.isEmpty()) {
            y += lineCurrentHeight

            x = 0
            xLast = 0
            yLast = y
            lineCurrentHeight = lineHeight
        } else {
            elements.forEach { element ->
                if (imageStrMap != null) {
                    element.filterNot { it == ')' }.split('=').let {
                        imageStrMap!!.put(it[0], it[1])
                    }

                    if (element.last() == ')') {
//                        println(imageStrMap)

                        when (imageStrMap!!["layout"]) {
                            "embedded" -> {
                                val width = imageStrMap!!["width"]!!.toInt()

                                if (x != 0 && (page[y][x - 1] != 'r' && charWidth + x + width > pageWidth) ||
                                    !(x != 0 && page[y][x - 1] != 'r') && (x + width > pageWidth)
                                ) {
                                    //if (!page[y].any { it == 'r' } || page[y].any { it == 'w' || it == 'e' }) {
                                    y += lineCurrentHeight
                                    //} else {
                                    //    y++
                                    //}
                                    x = 0
                                    lineCurrentHeight = lineHeight
                                }

                                while (x != 0 && page[y][x - 1] != 'r' && page[y].slice(
                                        x..<min(
                                            charWidth + x + width,
                                            pageWidth
                                        )
                                    ).any { it != '-' } ||
                                    !(x != 0 && page[y][x - 1] != 'r') && page[y].slice(x..<min(x + width, pageWidth))
                                        .any { it != '-' }
                                ) {
                                    x++
                                    if (
                                        x + width > pageWidth
                                    ) {
                                        //if (!page[y].any { it == 'r' } || page[y].any { it == 'w' || it == 'e' }) {
                                        y += lineCurrentHeight
                                        //} else {
                                        //    y++
                                        //}
                                        x = 0
                                        lineCurrentHeight = lineHeight
                                    }
                                }
                                lineCurrentHeight = max(imageStrMap!!["height"]!!.toInt(), lineCurrentHeight)

                                if (x != 0 && page[y][x - 1] != 'r') {
                                    x += charWidth
                                    for (j in y..<y + lineCurrentHeight) {
                                        for (i in x - charWidth..<x) {
                                            page[j][i] = 's'
                                        }
                                    }
                                }
                                fileOutput.appendText("$x ${y + yAdd}\n")

                                x += width
                                for (j in y..<y + lineCurrentHeight) {
                                    for (i in x - width..<x) {
                                        page[j][i] = 'e'
                                    }
                                }
                                xLast = x
                                yLast = y
                            }

                            "surrounded" -> {
                                val width = imageStrMap!!["width"]!!.toInt()

                                if (x + width > pageWidth) {
                                    //if (!page[y].any { it == 'r' } || page[y].any { it == 'w' || it == 'e' }) {
                                    y += lineCurrentHeight
                                    // } else {
                                    //    y++
                                    //}
                                    x = 0
                                    lineCurrentHeight = lineHeight
                                }

                                while (page[y].slice(x..<min(x + width, pageWidth)).any { it != '-' }) {
                                    x++
                                    if (x != 0 && (page[y][x - 1] != 'r' && charWidth + x + width > pageWidth) ||
                                        !(x != 0 && page[y][x - 1] != 'r') && (x + width > pageWidth)
                                    ) {
                                        //if (!page[y].any { it == 'r' } || page[y].any { it == 'w' || it == 'e' }) {
                                        y += lineCurrentHeight
                                        //} else {
                                        //    y++
                                        //}
                                        x = 0
                                        lineCurrentHeight = lineHeight
                                    }
                                }

                                fileOutput.appendText("$x ${y + yAdd}\n")


                                val height = imageStrMap!!["height"]!!.toInt()

                                x += width
                                for (j in y..<y + height) {
                                    for (i in x - width..<x) {
                                        page[j][i] = 'r'
                                    }
                                }
                                xLast = x
                                yLast = y
                            }

                            "floating" -> {
                                var xStart = max(xLast + imageStrMap!!["dx"]!!.toInt(), 0)
                                val xEnd = xStart + imageStrMap!!["width"]!!.toInt()

                                if (xEnd > pageWidth)
                                    xStart = pageWidth - imageStrMap!!["width"]!!.toInt()

//                                println(xStart.toString() + " " + (y + imageStrMap!!["dy"]!!.toInt()))

                                fileOutput.appendText(xStart.toString() + " " + (yLast + yAdd + imageStrMap!!["dy"]!!.toInt()) + '\n')
                                xLast = xStart + imageStrMap!!["width"]!!.toInt()
                                yLast = yLast + imageStrMap!!["dy"]!!.toInt()
                            }
                        }

                        imageStrMap = null
                    }

                } else if (element[0] == '(') {

                    imageStrMap = HashMap()

                } else {

                    val width = element.length * charWidth

                    if (x != 0 && (page[y][x - 1] != 'r' && charWidth + x + width > pageWidth) ||
                        !(x != 0 && page[y][x - 1] != 'r') && (x + width > pageWidth)
                    ) {
                        y += lineCurrentHeight
                        x = 0
                        lineCurrentHeight = lineHeight
                    }

                    while (x != 0 && page[y][x - 1] != 'r' && page[y].slice(x..<min(charWidth + x + width, pageWidth))
                            .any { it != '-' } ||
                        !(x != 0 && page[y][x - 1] != 'r') && page[y].slice(x..<min(x + width, pageWidth))
                            .any { it != '-' }
                    ) {
                        x++
                        if (x != 0 && (page[y][x - 1] != 'r' && charWidth + x + width > pageWidth) ||
                            !(x != 0 && page[y][x - 1] != 'r') && (x + width > pageWidth)
                        ) {
                            y += lineCurrentHeight
                            x = 0
                            lineCurrentHeight = lineHeight
                        }
                    }

                    if (x != 0 && (page[y][x - 1] != 'r' && charWidth + x + width > pageWidth) ||
                        !(x != 0 && page[y][x - 1] != 'r') && (x + width > pageWidth)
                    ) {
                        y += lineCurrentHeight
                        x = 0
                        lineCurrentHeight = lineHeight
                    }

                    if (x != 0 && page[y][x - 1] != 'r') {
                        x += charWidth
                        for (j in y..<y + lineCurrentHeight) {
                            for (i in x - charWidth..<x) {
                                page[j][i] = 's'
                            }
                        }
                    }
                    x += width
                    for (j in y..<y + lineCurrentHeight) {
                        for (i in x - width..<x) {
                            page[j][i] = 'w'
                        }
                    }
                    xLast = x
                    yLast = y
                }
                if (y > 1500) {
                    yAdd += 1000
                    yLast -= 1000
                    y -= 1000
                    page = ArrayList(page.subList(1000, page.size))
                    addEmptyLines(1000)
                }
            }
        }
        println("x = $x")
        println("y = $y")
        printPage(page)
    }
}


fun printPage(page: ArrayList<CharArray>) {
    page.forEachIndexed { index, line -> println("${String.format("%04d", index)}: ${line.joinToString("")}") }
}