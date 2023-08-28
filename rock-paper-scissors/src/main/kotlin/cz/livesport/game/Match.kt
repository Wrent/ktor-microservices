package cz.livesport.game

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
        // TODO
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
