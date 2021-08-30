package com.demo.book.api

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BookingIntegrationSpec
import com.demo.book.show.entity.Show
import com.demo.utils.getWithBasicAuth
import com.demo.utils.putWithBasicAuth
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
            createNewMovie(newMovieRequest(100), adminCredentials)
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
             |    "capacity" : 100,
             |    "price" : 0
             |  } ]
             |}""".trimMargin().trimIndent()
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

        "should return available seats with correct credentials" {
            createUser(adminCredentials)

            createNewMovie(newMovieRequest(100), adminCredentials).body.get()
            createNewShowWithBasicAuth(newShowRequest("2021-09-25", "2021-09-25T15:50:00"), adminCredentials)
            val response = httpClient.getWithBasicAuth<List<Int>>("/shows/1/seats", adminCredentials)
            // Then
            response.status shouldBe HttpStatus.OK
            val availableShows = response.body.get()
            jsonString(availableShows) shouldBe """
             |  [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100 ]
             """
                .trimMargin().trimIndent()
        }

        "should fail to return available shows with incorrect credentials" {
            // When
            val exception = shouldThrow<HttpClientResponseException> { getAvailableShowsWithAuth(nonAdminCredentials) }

            // Then
            exception.status shouldBe HttpStatus.UNAUTHORIZED
            exception.message shouldBe "Unauthorized"
        }

        "should get a show by id with correct credentials" {
            // Given
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(100), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest("2021-09-25", "2021-09-25T15:50:00"), adminCredentials)

            // When
            val response = httpClient.getWithBasicAuth<Show>("/shows/1", adminCredentials)

            // Then
            response.status shouldBe HttpStatus.OK
            val savedShows = response.body.get()
            jsonString(savedShows) shouldBe """
             |{
             |  "id" : 1,
             |  "movieId" : 1,
             |  "showDate" : "2021-09-25",
             |  "startTime" : "2021-09-25 15:50:00",
             |  "endTime" : "2021-09-25 17:30:00",
             |  "capacity" : 100,
             |  "price" : 0
             |}""".trimMargin().trimIndent()
        }

        "should update the price of show with correct credentials" {
            // Given
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(100), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest("2021-09-25", "2021-09-25T15:50:00"), adminCredentials)

            // When
            httpClient.putWithBasicAuth("/shows/1/price/100", "", adminCredentials)
            val response = httpClient.getWithBasicAuth<Show>("/shows/1", adminCredentials)

            // Then
            response.status shouldBe HttpStatus.OK
            val savedShows = response.body.get()
            savedShows.price shouldBe 100
        }

        "should throw an exception when price is less than 1" {
            // Given
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(100), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest("2021-09-25", "2021-09-25T15:50:00"), adminCredentials)

            // When
            val exception = shouldThrow<HttpClientResponseException> {
                httpClient.putWithBasicAuth(
                    "/shows/1/price/-100",
                    "",
                    adminCredentials
                )
            }

            // Then
            exception.message shouldBe "Price cannot be less than 1"
        }

        "should throw an exception when price is already defined" {
            // Given
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(100), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest("2021-09-25", "2021-09-25T15:50:00"), adminCredentials)

            // When
            httpClient.putWithBasicAuth("/shows/1/price/100", "", adminCredentials)
            val exception = shouldThrow<HttpClientResponseException> {
                httpClient.putWithBasicAuth(
                    "/shows/1/price/200",
                    "",
                    adminCredentials
                )
            }

            // Then
            exception.message shouldBe "Show price already defined"
        }
    }
}
