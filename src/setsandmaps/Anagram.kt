package setsandmaps

enum class Answers {
    NO,
    YES
}

fun main() {
    val line1 = readln()
    val line2 = readln()

    if (line1.length != line2.length) {
        println(Answers.NO)
        return
    }

    val map = HashMap<Char, Int>()

    line1.forEach { char ->
        map[char] = map.getOrDefault(char, 0) + 1
    }

    line2.forEach { char ->
        if (map.containsKey(char))
            map[char] = map[char]!! - 1
        else {
            println(Answers.NO)
            return
        }
    }

    if (map.values.any { it != 0 }) {
        println(Answers.NO)
    } else {
        println(Answers.YES)
    }
}