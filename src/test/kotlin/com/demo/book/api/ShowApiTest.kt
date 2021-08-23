package com.demo.book.api


import com.demo.book.BaseIntegrationSpec
import com.demo.book.movie.request.CreateShowRequest
import com.demo.book.show.entity.Show
import com.demo.book.utils.get
import com.demo.book.utils.post
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month


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
                |  "id": 1,
                |    "movieTitle": "Avengers EndGame",
                |}
            """.trimMargin().trimIndent()

        }

        "should add show " {
            // When
            val response = createNewShow(newShowRequest())

            // Then
            response.status shouldBe HttpStatus.OK

            val savedShows = response.body.get()
            response.body.get() shouldBe 1

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
             1, LocalDate.of(2021, Month.AUGUST, 23), LocalDateTime.of(2021, Month.AUGUST, 23, 9, 0), LocalDateTime.of(2021, Month.AUGUST, 23, 12, 0)
        )
    }

}
