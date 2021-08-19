package com.demo.book.api

import com.demo.book.BaseIntegrationSpec
import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.MovieRequest
import com.demo.book.utils.get
import com.demo.book.utils.post
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import java.time.ZoneId
import java.time.ZonedDateTime

class MovieApiTest() : BaseIntegrationSpec() {

    init {
        "should save movie" {
            // Given
            val referenceDate = ZonedDateTime.of(2021, 5, 21, 11, 15, 0, 0, ZoneId.systemDefault())
            val avengersMovie = newMovieRequest(
                referenceDate.toInstant().toEpochMilli(),
                referenceDate.plusHours(2).toInstant().toEpochMilli()
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
                referenceDate.toInstant().toEpochMilli(),
                referenceDate.plusHours(2).toInstant().toEpochMilli()
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
                |  "startTime" : "2021-06-01 09:15:00.000",
                |  "endTime" : "2021-06-01 11:15:00.000"
                |}
            """.trimMargin().trimIndent()
        }
    }

    private fun createNewMovie(avengersMovie: MovieRequest): HttpResponse<Any> {
        return httpClient.post(
            url = "/movies",
            body = jsonMapper.writeValueAsString(avengersMovie)
        )
    }

    private fun newMovieRequest(startTime: Long, endTime: Long): MovieRequest {
        return MovieRequest(
            "Avengers",
            startTime,
            endTime
        )
    }
}
