package binarysearch

import java.io.File

data class ElectionParty(
    val id: Int,
    var voters: Int,
    val price: Int
) : Comparable<ElectionParty> {
    override fun compareTo(other: ElectionParty): Int = -when {
        price == -1 && other.price == -1 -> voters - other.voters
        price == -1 -> -1
        other.price == -1 -> 1
        else -> voters - price - (other.voters - other.price)
    }

    override fun toString(): String {
        return "ElectionParty(voters=$voters, price=$price)"
    }
}


data class ElectionsResult(
    val price: Int,
    val party: ElectionParty,
    var voters: List<Int>
) {
    fun restoreVoters(partiesByVoters: List<ElectionParty>) {
        partiesByVoters.forEachIndexed { index, value -> value.voters = voters[index] }
        voters = partiesByVoters
            .sortedBy { it.id }
            .map { it.voters }
            .toMutableList()
            .also { it[party.id] += price - party.price }
    }

    override fun toString(): String {
        return "$price\n${party.id + 1}\n${voters.joinToString(" ")}"
    }
}


fun main() {
    val input = File("input.txt").bufferedReader()
    val output = File("output.txt")

    val n = input.readLine().toInt()
    val parties = List(n) { id ->
        input.readLine().split(' ').map { it.toInt() }.let {
            ElectionParty(id, it[0], it[1])
        }
    }.sorted()

    if (parties.size == 1) {
        println(ElectionsResult(parties.first().price, parties.first(), listOf(parties.first().voters)))
        return
    }

    val partiesByVoters = parties.sortedByDescending { it.voters }
    val voters = partiesByVoters.map { it.voters }

    var l = 0
    var r = parties.maxOf { it.voters } + parties.maxOf { it.price }
    var res: ElectionsResult? = null
    while (l < r) {
        val medium = (l + r) / 2
        res = check(parties, voters, medium)
        if (res != null) {
            r = medium
        } else {
            l = medium + 1
        }
    }
    if (res != null) {
        res.restoreVoters(partiesByVoters)
        println(res.toString())
    } else {
        println(ElectionsResult(parties.first().price, parties.first(), parties.map { it.voters }))
    }
}

fun check(parties: List<ElectionParty>, voters: List<Int>, money: Int): ElectionsResult? {
    for (party in parties) {
        simulate(party, voters, money)?.let { return it }
    }
    return null
}

fun simulate(party: ElectionParty, voters: List<Int>, money: Int): ElectionsResult? {
    if (party.price == -1)
        return null

    var cash = money - party.price
    if (cash < 0)
        return null

    val currentVoters = voters.toMutableList()

    val voter = party.voters + cash

    while (voter <= if (party.price != currentVoters.first()) currentVoters.first() else currentVoters[1]) {

        val max = if (party.price != currentVoters.first()) currentVoters.first() else currentVoters[1]
        var i = 0
        var ignored = false
        while (i < currentVoters.size && currentVoters[i] == max) {
            if (currentVoters[i] != party.voters || ignored) {
                currentVoters[i]--
                cash--
            } else {
                ignored = true
            }
            i++
        }

        if (cash < 0) break
    }

    if (cash < 0) return null
    else if (cash > 0) currentVoters[0]--
    return ElectionsResult(money, party, currentVoters)
}