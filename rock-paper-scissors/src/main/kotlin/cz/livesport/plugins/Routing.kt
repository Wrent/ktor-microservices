package cz.livesport.plugins

import cz.livesport.game.MatchSimulation
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureRouting(matchSimulation: MatchSimulation) {
    routing {
        get("/matches/rock-paper-scissors") {
            call.respond(matchSimulation.matches)
        }
    }
}
