package complexity

fun main() {
    val (team1Score1, team2Score1) = readln().split(':').map { it.toInt() }
    val (team1Score2, team2Score2) = readln().split(':').map { it.toInt() }
    val team1Score1AtHome = readln().toInt()

    val team1Score = team1Score1 + team1Score2
    val team2Score = team2Score1 + team2Score2

    if (team1Score > team2Score) {
        println(0)
        return
    }

    val dif = team2Score - team1Score

    if (team1Score1AtHome == 1) {
        println(dif + if (team1Score2 + dif <= team2Score1) 1 else 0)
    } else {
        println(dif + if (team1Score1 <= team2Score2) 1 else 0)
    }
}