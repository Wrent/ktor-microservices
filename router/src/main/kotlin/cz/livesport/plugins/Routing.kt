package cz.livesport.plugins

import cz.livesport.api.getGames
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/games/{projectId}/{lang}") {
            val projectId = call.parameters["projectId"] ?: "GL"
            val lang = call.parameters["lang"] ?: "EN"

            val games = getGames(projectId, lang)
            call.respond(games)
        }
    }
}
