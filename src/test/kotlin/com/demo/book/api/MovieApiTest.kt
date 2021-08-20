package com.demo.book.api

import com.demo.book.BaseIntegrationSpec
import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.utils.get
import com.demo.book.utils.post
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import java.time.ZoneId
import java.time.ZonedDateTime

class MovieApiTest : BaseIntegrationSpec() {

    init {
        "should save movie" {
            // Given
            val referenceDate = ZonedDateTime.of(2021, 5, 21, 11, 15, 0, 0, ZoneId.systemDefault())
            val avengersMovie = newMovieRequest(
                referenceDate.toInstant().toEpochMilli() / 60000,
                referenceDate.plusHours(2).toInstant().toEpochMilli() / 60000
            )

            // When
            val response = createNewMovie(avengersMovie)

            // Then
            response.status shouldBe HttpStatus.OK
            response.body.get() shouldBe 1
        }

        "should get all saved movies" {
            // Given
            val referenceDate = ZonedDateTime.of(2021, 6, 1, 9, 15, 0, 0, ZoneId.systemDefault())
            createNewMovie(newMovieRequest(
                referenceDate.toInstant().toEpochMilli() / 60000,
                referenceDate.plusHours(2).toInstant().toEpochMilli() / 60000
            ))

            // When
            val response = httpClient.get<List<Movie>>("/movies")

            // Then
            response.status shouldBe HttpStatus.OK
            val savedMovies = response.body.get()
            savedMovies.size shouldBe 1
            jsonString(savedMovies[0]) shouldBe """
                |{
                |  "id" : 1,
                |  "title" : "Avengers",
                |  "duration" : 120
                |}
            """.trimMargin().trimIndent()
        }

        "should fail for adding movie with invalid duration" {
            // Given
            val referenceDate = ZonedDateTime.of(2021, 6, 1, 9, 15, 0, 0, ZoneId.systemDefault())

            // When
            shouldThrow<HttpClientResponseException> { createNewMovie(newMovieRequest(
                referenceDate.toInstant().toEpochMilli() / 60000,
                referenceDate.plusHours(8).toInstant().toEpochMilli() / 60000
            )) }

            // Then
            val response = httpClient.get<List<Movie>>("/movies")
            response.status shouldBe HttpStatus.OK
            val savedMovies = response.body.get()
            savedMovies.size shouldBe 0
        }

        "should succeed for edge case 5 min" {
            // Given
            val referenceDate = ZonedDateTime.of(2021, 6, 1, 9, 15, 0, 0, ZoneId.systemDefault())

            // When
            val response = createNewMovie(newMovieRequest(
                referenceDate.toInstant().toEpochMilli() / 60000,
                referenceDate.plusMinutes(5).toInstant().toEpochMilli() / 60000
            ))

            // Then
            response.status shouldBe HttpStatus.OK
            val savedMovieId = response.body.get()
            savedMovieId shouldBe 1
        }

        "should succeed for edge case 6 hours" {
            // Given
            val referenceDate = ZonedDateTime.of(2021, 6, 1, 9, 15, 0, 0, ZoneId.systemDefault())

            // When
            val response = createNewMovie(newMovieRequest(
                referenceDate.toInstant().toEpochMilli() / 60000,
                referenceDate.plusHours(6).toInstant().toEpochMilli() / 60000
            ))

            // Then
            response.status shouldBe HttpStatus.OK
            val savedMovieId = response.body.get()
            savedMovieId shouldBe 1
        }
    }

    private fun createNewMovie(avengersMovie: CreateMovieRequest): HttpResponse<Any> {
        return httpClient.post(
            url = "/movies",
            body = jsonMapper.writeValueAsString(avengersMovie)
        )
    }

    private fun newMovieRequest(startTime: Long, endTime: Long): CreateMovieRequest {
        return CreateMovieRequest(
            "Avengers",
            (endTime - startTime).toInt()
        )
    }
}
