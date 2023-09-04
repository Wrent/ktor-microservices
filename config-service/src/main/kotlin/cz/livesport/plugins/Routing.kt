package cz.livesport.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val features = mapOf(
    "CZ" to listOf("RPS", "Football"),
    "GL" to listOf("RPS"),
    "BR" to listOf("Football")
)

val translates = mapOf(
    "CZ" to mapOf(
        "RPS" to "Kamen Nuzky Papir",
        "res" to "Vysledek"
    ),
    "EN" to mapOf(
        "RPS" to "Rock Paper Scissors",
        "res" to "Result"
    ),
)

fun Application.configureRouting() {
    routing {
        get("/features/{projectId}") {
            val projectId = call.parameters["projectId"]

            call.respond(features[projectId] ?: emptyList())
        }
        get("/translate") {
            val lang = call.request.queryParameters["lang"]
            val key = call.request.queryParameters["key"]

            val value = translates[lang]?.get(key)

            call.respond(mapOf("lang" to lang, "key" to key, "value" to value))
        }

    }
}
