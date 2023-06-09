package com.github.nagatsukaakiya.osuapi.ranking

import com.github.nagatsukaakiya.osuapi.auth.Token
import com.github.nagatsukaakiya.osuapi.models.GameMode
import com.github.nagatsukaakiya.osuapi.models.RankingType
import com.github.nagatsukaakiya.osuapi.models.Rankings
import com.github.nagatsukaakiya.osuapi.models.Spotlight
import com.github.nagatsukaakiya.osuapi.models.Spotlights
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

interface RankingApi {
    suspend fun getRanking(
        token: Token,
        mode: GameMode,
        type: RankingType,
        country: Int? = null,
        cursor: String? = null,
        filter: String? = null,
        spotlight: String? = null,
        variant: String? = null,
    ): Rankings

    suspend fun getSpotlights(token: Token): List<Spotlight>
}

internal class RankingApiImpl(private val client: HttpClient) : RankingApi {
    companion object {
        private const val rankings = "https://osu.ppy.sh/api/v2/rankings"
        private const val spotlights = "https://osu.ppy.sh/api/v2/spotlights"
    }

    override suspend fun getRanking(
        token: Token,
        mode: GameMode,
        type: RankingType,
        country: Int?,
        cursor: String?,
        filter: String?,
        spotlight: String?,
        variant: String?
    ): Rankings {
        return client.get("$rankings/${mode.getValue()}/${type.getValue()}") {
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.ContentType, "application/json")
                bearerAuth(token.value)
            }
            parameter("country", country)
            parameter("cursor", cursor)
            parameter("filter", filter)
            parameter("spotlight", spotlight)
            parameter("variant", variant)
        }.body()
    }

    override suspend fun getSpotlights(token: Token): List<Spotlight> {
        return client.get(spotlights) {
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.ContentType, "application/json")
                bearerAuth(token.value)
            }
        }.body<Spotlights>().spotlights
    }
}
