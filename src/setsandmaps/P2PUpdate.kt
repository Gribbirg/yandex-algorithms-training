package setsandmaps

import java.io.File

fun main() {
    val input = File("input.txt").bufferedReader()
    val (n, packetsCount) = input.readLine().split(' ').map { it.toInt() }

    val elements = MutableList(n) { MutableList(packetsCount) { false } }
    elements[0] = MutableList(packetsCount) { true }

    val closedOn = MutableList(n) { -1 }
    closedOn[0] = 0

    val packetsCountList = MutableList(packetsCount) { 1 }

    val packetsCountListByElement = MutableList(n) { 0 }
    packetsCountListByElement[0] = packetsCount

    val receivedFrom = MutableList(n) { HashMap<Int, Int>() }

    var closedCount = 1

    var i = 1
    while (closedCount < n) {

        val requests = HashMap<Int, ArrayList<List<Int>>>()
        val packetsSorted = List(packetsCount) { listOf(it, packetsCountList[it]) }.sortedBy { it[1] }
        elements.forEachIndexed { index, packets ->
            if (closedOn[index] == -1) {
                val packet =
                    packetsSorted
                        .first { !packets[it[0]] }[0]
                val requestTo =
                    elements
                        .indices
                        .filter { i -> elements[i][packet] && i != index }
                        .minBy { i -> packetsCountListByElement[i] }
                if (!requests.containsKey(requestTo)) requests[requestTo] = ArrayList()
                requests[requestTo]!!.add(listOf(index, packet))
            }
        }

        val transfers = HashMap<Int, List<Int>>()
        requests.forEach { (requestTo, requestsList) ->
            transfers[requestTo] = requestsList.maxWith(
                compareBy(
                    { (request, _) -> receivedFrom[requestTo][request] },
                    { (request, _) -> -packetsCountListByElement[request] }
                )
            )
        }

        transfers.forEach { (requestTo, list) ->
            val (requestFrom, packet) = list
            elements[requestFrom][packet] = true

            packetsCountList[packet]++
            packetsCountListByElement[requestFrom]++
            receivedFrom[requestFrom][requestTo] = receivedFrom[requestFrom].getOrDefault(requestTo, 0) + 1
        }

        elements.indices.forEach { index ->
            if (closedOn[index] == -1 && packetsCountListByElement[index] == packetsCount) {
                closedOn[index] = i
                closedCount++
            }
        }

        i++
    }
    println(closedOn.filterIndexed { j, _ -> j != 0 }.joinToString(" "))
}