package linearsearch

import java.io.File
import kotlin.math.min


data class Character(
    val power: Int = 0,
    val race: Int = 0,
    val `class`: Int = 0
) : Comparable<Character> {
    override fun compareTo(other: Character): Int = when {
        power != other.power -> other.power - power
        else -> 0
    }

    override fun toString(): String {
        return "Character(power=$power, race=$race, `class`=$`class`)"
    }
}

fun main() {
    val input = File("input.txt").bufferedReader()
    val (raceCount, classCount) = input.readLine().split(' ').map { it.toInt() }
    val characters = ArrayList<Character>()

    repeat(raceCount) { race ->
        input.readLine().split(' ').map {
            it.toInt()
        }.forEachIndexed { `class`, power ->
            characters.add(Character(power, race, `class`))
        }
    }

    characters.sort()
    var maxValue = -1
    var res = ""

    val charactersWithMaxPower = characters.filter { it.power == characters.first().power }
    for (powerful in charactersWithMaxPower.subList(0, min(charactersWithMaxPower.size, 10))) {

        val charactersOtherRace = characters.first { it.race != powerful.race }
        val charactersRaceMax =
            characters.first { it.race != powerful.race && it.`class` != charactersOtherRace.`class` }

        val charactersOtherClass = characters.first { it.`class` != powerful.`class` }
        val charactersClassMax =
            characters.first { it.`class` != powerful.`class` && it.race != charactersOtherClass.race }

        if (charactersRaceMax.power <= charactersClassMax.power) {
            if (charactersRaceMax.power > maxValue) {
                maxValue = charactersRaceMax.power
                res = "${powerful.race + 1} ${charactersOtherRace.`class` + 1}"
            }
        } else {
            if (charactersClassMax.power > maxValue) {
                maxValue = charactersClassMax.power
                res = "${charactersOtherClass.race + 1} ${powerful.`class` + 1}"
            }
        }
    }
    println(res)
}