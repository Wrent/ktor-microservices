package cz.livesport

import cz.livesport.game.MatchSimulation
import cz.livesport.plugins.*
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.newFixedThreadPoolContext

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val matchSimulation = MatchSimulation()
    val context = newFixedThreadPoolContext(1, "simu")
    CoroutineScope(context).async {
        matchSimulation.simulate()
    }
    install(ContentNegotiation) {
        jackson()
    }
    configureRouting(matchSimulation)
}
