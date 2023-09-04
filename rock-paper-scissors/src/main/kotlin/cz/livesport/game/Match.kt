package cz.livesport.game

import kotlinx.coroutines.delay

data class Match(
    val participantA: MatchParticipant,
    val participantB: MatchParticipant,
    var rounds: Int,
    var isFinished: Boolean,
)

data class MatchParticipant(
    val name: String,
    var score: Int,
)

class MatchSimulation {

    val matches: MutableList<Match> = mutableListOf()

    suspend fun simulate() {
        var current: Match? = null

        while (true) {
            if (current == null || current.isFinished) {
                current = newMatch()
                matches.add(current)
            }
            makeMove(current)
            evaluateMatchEnd(current)
            delay(4000)
        }
    }

    private fun makeMove(current: Match) {
        val symbolA = Symbol.entries.random()
        val symbolB = Symbol.entries.random()

        if (symbolA.beats() == symbolB) {
            current.participantA.score++
        } else if (symbolB.beats() == symbolA) {
            current.participantB.score++
        }

        current.rounds++
    }

    private fun evaluateMatchEnd(current: Match) {
        if (current.participantA.score == 3 || current.participantB.score == 3) {
            current.isFinished = true
        }
    }

    private fun newMatch(): Match {
        val (participantA, participantB) = getParticipants()
        return Match(
            participantA = participantA,
            participantB = participantB,
            rounds = 0,
            isFinished = false,
        )
    }

    private fun getParticipants(): Pair<MatchParticipant, MatchParticipant> {
        val participantA = participants.random()
        val participantB = participants.minus(participantA).random()

        return MatchParticipant(participantA, 0) to MatchParticipant(participantB, 0)
    }

    private val participants = setOf(
        "jonas", "roman", "marek", "jan", "bara", "petra", "zuzana", "kaja", "marie"
    )
}

enum class Symbol {
    ROCK,
    PAPER,
    SCISSORS
}

fun Symbol.beats(): Symbol {
    return when (this) {
        Symbol.ROCK -> Symbol.SCISSORS
        Symbol.PAPER -> Symbol.ROCK
        Symbol.SCISSORS -> Symbol.PAPER
    }
}
