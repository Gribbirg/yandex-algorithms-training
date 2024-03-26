package binarysearch

import java.io.File

data class ElectionParty(
    val id: Int,
    var voters: Long,
    val price: Int,
    var winPrice: Long = Long.MAX_VALUE
) : Comparable<ElectionParty> {
    override fun compareTo(other: ElectionParty): Int = when {
        voters != other.voters -> (voters - other.voters).toInt()
        else -> id - other.id
    }

    override fun toString(): String {
        return "ElectionParty(id=$id, voters=$voters, price=$price, winPrice=$winPrice)"
    }
}


fun main() {
    val input = File("input.txt").bufferedReader()
    val output = File("output.txt")

    val n = input.readLine().toInt()
    val parties = List(n) { id ->
        input.readLine().split(' ').map { it.toLong() }.let {
            ElectionParty(id, it[0], it[1].toInt())
        }
    }.sorted()

    if (parties.size == 1) {
        output.writeText("${parties.first().price}\n${parties.first().id + 1}\n${parties.first().voters}")
        return
    }

    val voters = parties.map { it.voters }.toMutableList()
    (voters.lastIndex - 1 downTo 0).forEach { i ->
        voters[i] += voters[i + 1]
    }

    parties.forEach { party ->

        if (party.price == -1) {
            return@forEach
        }

        var l = party.voters
        var r = parties.last().voters
        if (parties.last().voters == parties[parties.lastIndex - 1].voters)
            r++

        while (l < r) {
            val medium = (l + r) / 2
            if (check(parties, voters, party, medium)) {
                r = medium
            } else {
                l = medium + 1
            }
        }
        party.winPrice = l + party.price - party.voters
    }

    val minParty = parties.minBy { it.winPrice }
    val resVoters = simulate(parties, minParty)
    output.writeText("${minParty.winPrice}\n${minParty.id + 1}\n${resVoters.joinToString(" ")}")
}

fun check(parties: List<ElectionParty>, voters: List<Long>, party: ElectionParty, money: Long): Boolean {
    var l = 0
    var r = parties.size
    while (l < r) {
        val medium = (l + r) / 2
        if (parties[medium].voters >= money) {
            r = medium
        } else {
            l = medium + 1
        }
    }
    return voters[l] - (parties.size - l) * (money - 1) + party.voters <= money
}

fun simulate(parties: List<ElectionParty>, party: ElectionParty): List<Long> {

    var cash = party.winPrice - party.price
    var voter = party.voters
    party.voters += cash.toInt()

    while (voter <= if (party.id != parties.last().id) parties.last().voters else parties[parties.lastIndex - 1].voters) {

        val max = (if (party.id != parties.last().id) parties.last() else parties[parties.lastIndex - 1]).voters
        var i = parties.lastIndex

        while (i >= 0 && cash > 0 && (parties[i].id == party.id || parties[i].voters == max)) {
            if (parties[i].id != party.id) {
                parties[i].voters--
                cash--
                voter++
            }
            i--
        }
        if (cash == 0L) break
    }
    if (cash > 0) (if (party.id != parties.last().id) parties.last() else parties[parties.lastIndex - 1]).voters--
    return parties.sortedBy { it.id }.map { it.voters }
}