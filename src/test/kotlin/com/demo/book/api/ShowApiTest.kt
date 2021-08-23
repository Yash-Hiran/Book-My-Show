package com.demo.book.api


import com.demo.book.BaseIntegrationSpec
import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.movie.request.CreateShowRequest
import com.demo.book.show.entity.Show
import com.demo.book.utils.get
import com.demo.book.utils.post
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.extensions.time.withConstantNow
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ShowApiTest : BaseIntegrationSpec() {

    init {

        "should get all saved shows" {
            // When
            createNewShow(newShowRequest())
            val response = httpClient.get<List<Show>>("/shows")

            // Then
            response.status shouldBe HttpStatus.OK
            val savedShows = response.body.get()
            savedShows.size shouldBe 1
            jsonString(savedShows[0]) shouldBe """
                |{
                |  "id" : 1,
                |  "title" : "Avengers",
                |  "duration" : 100
                |}
            """.trimMargin().trimIndent()

        }
        
    }
    private fun createNewShow(show: CreateShowRequest): HttpResponse<Any> {
        return httpClient.post(
            url = "/shows",
            body = jsonMapper.writeValueAsString(show)
        )
    }

    private fun newShowRequest(): CreateShowRequest {
        return CreateShowRequest(
             1, LocalDate.now() , LocalDateTime.now() ,  LocalDateTime.now()
        )
    }

}
