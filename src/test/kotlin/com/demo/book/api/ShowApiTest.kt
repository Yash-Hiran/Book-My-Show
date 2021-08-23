package com.demo.book.api

import com.demo.book.BaseIntegrationSpec
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.movie.request.CreateShowRequest
import com.demo.book.show.entity.Show
import com.demo.book.utils.get
import com.demo.book.utils.post
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import java.sql.Date
import java.sql.Timestamp

class ShowApiTest : BaseIntegrationSpec() {

    init {

        "should get all saved shows" {
            // When
            createNewMovie(newMovieRequest(100)).body.get()
            createNewShow(newShowRequest())

            val response = httpClient.get<List<Show>>("/shows")
            // Then
            response.status shouldBe HttpStatus.OK
            val savedShows = response.body.get()
            savedShows.size shouldBe 1
            jsonString(savedShows[0]) shouldBe """
                |{
                |  "id" : 1,
                |  "movieId" : 1,
                |  "showDate" : 1629849600000,
                |  "startTime" : 1629800436068,
                |  "endTime" : 1629806436068
                |}
            """.trimMargin().trimIndent()
        }

        "should add show " {
            // When
            createNewMovie(newMovieRequest(100)).body.get()
            createNewShow(newShowRequest())
            val response = createNewShow(newShowRequest())

            // Then
            response.status shouldBe HttpStatus.OK

            val savedShows = response.body.get()
            response.body.get() shouldBe 2
        }
    }

    private fun createNewShow(show: CreateShowRequest): HttpResponse<Any> {
        return httpClient.post(
            url = "/shows",
            body = jsonString(show)
        )
    }

    private fun newShowRequest(): CreateShowRequest {
        return CreateShowRequest(
            1,
            Date.valueOf("2021-08-25"),
            Timestamp.valueOf("2021-08-24 15:50:36.0680763")
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
