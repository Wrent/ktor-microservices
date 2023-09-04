package cz.livesport.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

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

data class Games(
    val matches: List<Match>,
    val title: String,
    val resultLabel: String,
)

private const val RPS_SERVICE = "http://localhost:8081"
private const val CONFIG_SERVICE = "http://localhost:8082"

suspend fun getGames(projectId: String, lang: String): Games {
    val features = fetch("$CONFIG_SERVICE/features/$projectId", object : TypeReference<Set<String>>() {}) ?: emptySet()
    if (features.contains("RPS")) {
        val title =
            coroutineScope {
                async {
                    fetch("$CONFIG_SERVICE/translate?lang=$lang&key=RPS", object : TypeReference<Translation>() {})
                }
            }
        val resultLabel =
            coroutineScope {
                async {
                    fetch("$CONFIG_SERVICE/translate?lang=$lang&key=res", object : TypeReference<Translation>() {})
                }
            }
        val matches = coroutineScope {
            async {
                fetch("$RPS_SERVICE/matches/rock-paper-scissors", object : TypeReference<List<Match>>() {})
                    ?: emptyList()
            }
        }

        return Games(matches.await(), title.await()?.value ?: "RPS", resultLabel.await()?.value ?: "res")
    } else {
        return Games(emptyList(), "", "")
    }
}

data class Translation(
    val key: String,
    val lang: String,
    val value: String,
)

private val client = OkHttpClient.Builder().build()
private val mapper = ObjectMapper().registerKotlinModule()

private suspend fun <T> fetch(url: String, typeReference: TypeReference<T>): T? {
    return withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val json = response.body?.string()
        json?.let { mapper.readValue(json, typeReference) }
    }
}
