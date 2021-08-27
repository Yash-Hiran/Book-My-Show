package com.demo.book.api

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BookingIntegrationSpec
import com.demo.book.show.entity.Show
import com.demo.utils.getWithBasicAuth
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import norm.executeCommand

@MicronautTest
class ShowApiTest : BookingIntegrationSpec() {
    override fun clearData() = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE shows RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE movies RESTART IDENTITY CASCADE;")
    }

    init {
        val adminCredentials = UserCredentialsRequest("admin", "kaiser")
        val nonAdminCredentials = UserCredentialsRequest("foo", "bar")

        "should get all upcoming shows with correct credentials" {

            // When
            createUser(adminCredentials)

            createNewMovie(newMovieRequest(100), adminCredentials).body.get()
            createNewShowWithBasicAuth(newShowRequest("2021-09-25", "2021-09-25T15:50:00"), adminCredentials)
            val response = httpClient.getWithBasicAuth<Map<String, List<Show>>>("/shows", adminCredentials)
            // Then
            response.status shouldBe HttpStatus.OK
            val savedShows = response.body.get()
            jsonString(savedShows) shouldBe """
             |{
             |  "Upcoming:" : [ {
             |    "id" : 1,
             |    "movieId" : 1,
             |    "showDate" : "2021-09-25",
             |    "startTime" : "2021-09-25 15:50:00",
             |    "endTime" : "2021-09-25 17:30:00",
             |    "price" : 0
             |  } ]
             |}"""
                .trimMargin().trimIndent()
        }

        "should get an empty Map when there are no saved shows with correct credentials" {
            // Given
            createUser(adminCredentials)

            // When
            val response = getAllShowsWithAuth(adminCredentials)

            response.status shouldBe HttpStatus.OK
            val savedShows = response.body.get()
            savedShows.size shouldBe 0
            jsonString(savedShows) shouldBe "{ }"
        }

        "should add show with correct credentials" {
            // When
            createUser(adminCredentials)
            val startDate = "2021-09-26"
            val startTime = "2021-09-26T15:50:36.0680763"
            createNewMovie(newMovieRequest(100), adminCredentials)
            val response = createNewShowWithBasicAuth(newShowRequest(startDate, startTime), adminCredentials)

            // Then
            response.status shouldBe HttpStatus.OK

            val savedShows = response.body.get()
            savedShows shouldBe "1"
        }

        "should fail to add show with incorrect credentials" {
            // Given
            createUser(adminCredentials)
            val startDate = "2021-08-26"
            val startTime = "2021-08-26T15:50:36.0680763"
            createNewMovie(newMovieRequest(100), adminCredentials)

            // When
            val exception = shouldThrow<HttpClientResponseException>
            { createNewShowWithBasicAuth(newShowRequest(startDate, startTime), nonAdminCredentials) }

            // Then
            exception.status shouldBe HttpStatus.UNAUTHORIZED
            exception.message shouldBe "Unauthorized"
        }

        "should fail to get shows with incorrect credentials" {
            // When
            val exception = shouldThrow<HttpClientResponseException> { getAllShowsWithAuth(nonAdminCredentials) }

            // Then
            exception.status shouldBe HttpStatus.UNAUTHORIZED
            exception.message shouldBe "Unauthorized"
        }
    }
}
