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
import java.time.format.DateTimeFormatter

class ShowApiTest : BaseIntegrationSpec() {

    init {

        "should get all saved shows" {
            // When
            val string = "2017-07-25"
            val showDate = LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
            createNewShow(newShowRequest(showDate))
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
        
    }
    private fun createNewShow(show: CreateShowRequest): HttpResponse<Any> {
        return httpClient.post(
            url = "/shows",
            body = jsonMapper.writeValueAsString(show)
        )
    }

    private fun newShowRequest(showDate : LocalDate): CreateShowRequest {
        return CreateShowRequest(
             1, showDate, LocalDateTime.now() ,  LocalDateTime.now()
        )
    }

}
