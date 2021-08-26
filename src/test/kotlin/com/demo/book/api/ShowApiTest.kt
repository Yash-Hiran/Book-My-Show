package com.demo.book.api

import com.demo.book.BaseIntegrationSpec
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.show.request.CreateShowRequest
import com.demo.book.show.entity.Show
import com.demo.book.utils.get
import com.demo.book.utils.post
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import java.time.LocalDate
import java.time.LocalDateTime

class ShowApiTest : BaseIntegrationSpec() {

    init {

        "should get all saved shows" {
            // When
            val startDate = "2030-08-25"
            val startTime = "2030-08-25T15:50:36.0680763"

            createNewMovie(newMovieRequest(100)).body.get()
            createNewShow(newShowRequest(startDate, startTime))

            val response = httpClient.get<List<Show>>("/shows")
            // Then
            response.status shouldBe HttpStatus.OK
            val savedShows = response.body.get()
            savedShows.size shouldBe 1
            jsonString(savedShows[0]) shouldBe """
                |{
                |  "id" : 1,
                |  "movieId" : 1,
                |  "showDate" : "2030-08-25",
                |  "startTime" : "2030-08-25 15:50:36",
                |  "endTime" : "2030-08-25 17:30:36"
                |}
            """.trimMargin().trimIndent()
        }

        "should get an empty List when there are no saved shows" {
            // When

            val response = httpClient.get<List<Show>>("/shows")
            response.status shouldBe HttpStatus.OK
            val savedShows = response.body.get()
            savedShows.size shouldBe 0
            jsonString(savedShows) shouldBe """
                [ ]
            """.trimMargin().trimIndent()
        }

        "should add show " {
            // When
            val startDate = "2031-08-26"
            val startTime = "2031-08-26T15:50:36.0680763"
            createNewMovie(newMovieRequest(100)).body.get()
            val response = createNewShow(newShowRequest(startDate, startTime))

            // Then
            response.status shouldBe HttpStatus.OK

            val savedShows = response.body.get()
            savedShows shouldBe 1
        }
    }

    private fun createNewShow(show: CreateShowRequest): HttpResponse<Any> {
        return httpClient.post(
            url = "/shows",
            body = jsonString(show)
        )
    }

    private fun newShowRequest(startDate: String, startTime: String): CreateShowRequest {
        return CreateShowRequest(
            1,
            LocalDate.parse(startDate),
            LocalDateTime.parse(startTime)
        )
    }

    private fun createNewMovie(avengersMovie: CreateMovieRequest): HttpResponse<String> {
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
