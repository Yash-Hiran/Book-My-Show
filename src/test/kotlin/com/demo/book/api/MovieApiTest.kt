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

class MovieApiTest : BaseIntegrationSpec() {

    init {
        "should save movie" {
            // When
            val response = createNewMovie(newMovieRequest(100))

            // Then
            response.status shouldBe HttpStatus.OK
            response.body.get() shouldBe 1
        }

        "should get all saved movies" {
            // When
            createNewMovie(newMovieRequest(100))
            val response = httpClient.get<List<Movie>>("/movies")

            // Then
            response.status shouldBe HttpStatus.OK
            val savedMovies = response.body.get()
            savedMovies.size shouldBe 1
            jsonString(savedMovies[0]) shouldBe """
                |{
                |  "id" : 1,
                |  "title" : "Avengers",
                |  "duration" : 100
                |}
            """.trimMargin().trimIndent()
        }
    }

    private fun createNewMovie(avengersMovie: CreateMovieRequest): HttpResponse<Any> {
        return httpClient.post(
            url = "/movies",
            body = jsonString(avengersMovie)
        )
    }

    private fun newMovieRequest(duration: Int): CreateMovieRequest {
        return CreateMovieRequest(
            "Avengers",
            duration
        )
    }
}
