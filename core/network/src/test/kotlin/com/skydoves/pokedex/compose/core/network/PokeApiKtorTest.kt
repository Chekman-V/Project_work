package com.skydoves.pokedex.compose.core.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokeApiKtorTests {
  private val client = HttpClient(CIO) {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
      })
    }
  }
  private val baseUrl = "https://pokeapi.co/api/v2"

  @Test
  fun get_pokemon_list_success() = runBlocking {
    val response: HttpResponse = client.get("$baseUrl/pokemon") {
      url {
        parameters.append("limit", "20")
        parameters.append("offset", "0")
      }
    }

    assertEquals(HttpStatusCode.OK, response.status)

    val responseBody = response.bodyAsText()
    assertTrue("Response should contain 'results'", responseBody.contains("\"results\""))
  }

  @Test
  fun get_pokemon_list_error() = runBlocking {
    val response: HttpResponse = client.get("$baseUrl/pokemon_invalid")
    assertEquals(HttpStatusCode.BadRequest, response.status)
  }

  @Test
  fun get_pokemon_info_success() = runBlocking {
    val pokemonName = "bulbasaur"
    val pokemonID = "1"

    val response: HttpResponse = client.get("$baseUrl/pokemon/$pokemonName")

    assertEquals(HttpStatusCode.OK, response.status)

    val responseBody = response.bodyAsText()
    assertTrue("Response should contain pokemon name", responseBody.contains("\"name\":\"$pokemonName\""))
  }

  @Test
  fun get_pokemon_info_error() = runBlocking {
    val invalidPokemonName = "unknown_pokemon"

    val response: HttpResponse = client.get("$baseUrl/pokemon/$invalidPokemonName")

    assertEquals(HttpStatusCode.NotFound, response.status)
  }

  @Test
  fun get_pokemon_type_success() = runBlocking {
    val typeName = "fire"

    val response: HttpResponse = client.get("$baseUrl/type/$typeName")

    assertEquals(HttpStatusCode.OK, response.status)

    val responseBody = response.bodyAsText()
    assertTrue("Response should contain type name", responseBody.contains("\"name\":\"$typeName\""))
  }

  @Test
  fun get_pokemon_type_error() = runBlocking {
    val invalidTypeName = "invalid_type"

    val response: HttpResponse = client.get("$baseUrl/type/$invalidTypeName")

    assertEquals(HttpStatusCode.NotFound, response.status)
  }
}
