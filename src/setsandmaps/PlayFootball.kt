package setsandmaps

import java.io.File

class PlayFootballDataBase {

    data class Goal(
        val gameId: Int,
        val team: String,
        val player: String = "",
        val minute: Int = -1,
        val isFirst: Boolean = false
    ) {
        override fun toString(): String {
            return "Goal(gameId=$gameId, team='$team', player='$player', minute=$minute, isFirst=$isFirst)"
        }
    }

    private val goals = ArrayList<Goal>()
    private fun getTotalByTeam(team: String) =
        goals.count { it.team == team && it.minute != -1 }

    private fun getTeamGamesCount(team: String) =
        goals.filter { it.team == team }.map { it.gameId }.toSet().size.toDouble()

    private fun getMeanByTeam(team: String) =
        getTeamGamesCount(team).let { if (it != .0) getTotalByTeam(team) / it else .0 }

    private fun getTotalByPlayer(player: String) =
        goals.count { it.player == player && it.minute != -1 }

    private fun getMeanByPlayer(player: String) =
        getPlayerTeam(player)?.let { getTotalByPlayer(player) /  getTeamGamesCount(it) } ?: 0

    private fun getOnMinuteByPlayer(player: String, minute: Int) =
        goals.count { it.player == player && it.minute == minute }

    private fun getOnFirstMinutesByPlayer(player: String, toMinute: Int) =
        goals.count { it.player == player && it.minute <= toMinute }

    private fun getOnLastMinutesByPlayer(player: String, fromMinute: Int) =
        goals.count { it.player == player && it.minute >= fromMinute }

    private fun getOpensByTeam(team: String) =
        goals.count { it.team == team && it.isFirst }

    private fun getOpensByPlayer(player: String) =
        goals.count { it.player == player && it.isFirst }

    private fun getGamesCount() =
        goals.map { it.gameId }.toSet().size

    private fun getPlayerTeam(player: String) =
        goals.firstOrNull { it.player == player }?.team

    private fun addData(text: List<String>, index: Int): Int {
        var i = index
        val teamNames = text[i].split('"').let { listOf(it[1], it[3]) }
        val goalsCount = text[i].split(' ').last().split(":").map { it.toInt() }
        i++

        val gameNum = getGamesCount()

        for (j in 0..1) {
            if (goalsCount[j] == 0)
                goals.add(
                    Goal(
                        gameId = gameNum,
                        team = teamNames[j]
                    )
                )
        }

        if (goalsCount.sum() == 0) {
            return i
        }

        val firstIndex = if (goalsCount[1] != 0) listOf(i, i + goalsCount[0]).minBy {
            text[it].split(' ').last().removeSuffix("\'").toInt()
        } else i

        goalsCount.forEachIndexed { j, value ->
            val start = i
            while (i != start + value) {
                val line = text[i].split(' ')
                val team = teamNames[j]
                val player = line.subList(0, line.lastIndex).joinToString(" ")

                goals.add(
                    Goal(
                        gameId = gameNum,
                        team = team,
                        player = player,
                        minute = line.last().removeSuffix("\'").toInt(),
                        isFirst = i == firstIndex
                    )
                )

                i++
            }
        }

        return i
    }

    private fun getData(line: String) {
        if (line.startsWith("Total goals for")) {
            println(getTotalByTeam(getTeam(line)))
        } else if (line.startsWith("Total goals by")) {
            println(getTotalByPlayer(getPlayer(line, 3)))
        } else if (line.startsWith("Mean goals per game for")) {
            println(getMeanByTeam(getTeam(line)))
        } else if (line.startsWith("Mean goals per game")) {
            println(getMeanByPlayer(getPlayer(line, 5)))
        } else if (line.startsWith("Goals on minute")) {
            println(getOnMinuteByPlayer(getPlayer(line, 5), getMinute(line)))
        } else if (line.startsWith("Goals on first")) {
            println(getOnFirstMinutesByPlayer(getPlayer(line, 6), getMinute(line)))
        } else if (line.startsWith("Goals on last")) {
            println(getOnLastMinutesByPlayer(getPlayer(line, 6), 91 - getMinute(line)))
        } else if (line.startsWith("Score opens by") && line.contains('"')) {
            println(getOpensByTeam(getTeam(line)))
        } else if (line.startsWith("Score opens by")) {
            println(getOpensByPlayer(getPlayer(line, 3)))
        }
    }

    fun readText(text: List<String>) {
        var i = 0

        while (i < text.size) {
            if (text[i].contains(':')) {
                i = addData(text, i)
            } else {
                getData(text[i])
                i++
            }
        }
    }

    companion object {
        private fun getTeam(line: String) =
            line.split('"')[1]

        private fun getPlayer(line: String, fromInSplit: Int) =
            line.split(' ').subList(fromInSplit, line.split(' ').size).joinToString(" ")

        private fun getMinute(line: String) =
            line.split(' ')[3].toInt()
    }
}

fun main() {
    val input = File("input.txt")
    val db = PlayFootballDataBase()
    db.readText(input.readLines())
}