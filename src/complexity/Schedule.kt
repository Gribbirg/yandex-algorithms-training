package complexity

import java.time.LocalDate

fun main() {
    val n = readln().toInt()
    val year = readln().toInt()
    val isLeap = (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)

    val daysOfWeekCountList = MutableList(7) { 0 }
    repeat(n) {
        val line = readln().split(' ')
        daysOfWeekCountList[LocalDate.of(year, getMonthNum(line[1]), line[0].toInt()).dayOfWeek.value - 1]--
    }

    val yearStart = getWeekDayNum(readln()) - 1
    daysOfWeekCountList[yearStart]++
    if (isLeap) daysOfWeekCountList[(yearStart + 1) % 7]++
    println(
        "${getWeekDayName(daysOfWeekCountList.indices.maxBy { daysOfWeekCountList[it] } + 1)} ${
            getWeekDayName(daysOfWeekCountList.indices.minBy { daysOfWeekCountList[it] } + 1)
        }"
    )
}


fun getMonthNum(name: String) = when (name) {
    "January" -> 1
    "February" -> 2
    "March" -> 3
    "April" -> 4
    "May" -> 5
    "June" -> 6
    "July" -> 7
    "August" -> 8
    "September" -> 9
    "October" -> 10
    "November" -> 11
    "December" -> 12
    else -> 0
}

fun getWeekDayNum(name: String) = when (name) {
    "Monday" -> 1
    "Tuesday" -> 2
    "Wednesday" -> 3
    "Thursday" -> 4
    "Friday" -> 5
    "Saturday" -> 6
    "Sunday" -> 7
    else -> 0
}

fun getWeekDayName(num: Int) = when (num) {
    1 -> "Monday"
    2 -> "Tuesday"
    3 -> "Wednesday"
    4 -> "Thursday"
    5 -> "Friday"
    6 -> "Saturday"
    7 -> "Sunday"
    else -> ""
}