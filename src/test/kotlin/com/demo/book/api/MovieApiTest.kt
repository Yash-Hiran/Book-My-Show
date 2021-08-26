package com.demo.book.api

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BookingIntegrationSpec
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import norm.executeCommand
import javax.sql.DataSource

@MicronautTest
class MovieApiTest(override var dataSource: DataSource) : BookingIntegrationSpec() {
    override fun clearData() = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE movies RESTART IDENTITY CASCADE;")
    }

    init {
        val adminCredentials = UserCredentialsRequest("Yash", "Mihir")
        val nonAdminCredentials = UserCredentialsRequest("Mihir", "Yash")

        "should save movie with correct credentials" {
            // Given
            createUser(adminCredentials)
            val requestCredentials = adminCredentials

            // When
            val response = createNewMovie(newMovieRequest(100), requestCredentials)

            // Then
            response.status shouldBe HttpStatus.OK
            response.body.get() shouldBe "1"
        }

        "should get all saved movies with correct credentials" {
            // Given
            createUser(adminCredentials)

            // When
            val response = createNewMovie(newMovieRequest(100), adminCredentials)

            // Then
            response.status shouldBe HttpStatus.OK
            val savedMovies = getAllMoviesWithAuth(adminCredentials).body.get()
            savedMovies.size shouldBe 1
            jsonString(savedMovies[0]) shouldBe """
                |{
                |  "id" : 1,
                |  "title" : "Avengers",
                |  "duration" : 100
                |}
            """.trimMargin().trimIndent()
        }

        "should get empty list if no movies are present with correct credentials" {
            // Given
            createUser(adminCredentials)

            // Then
            val savedMovies = getAllMoviesWithAuth(adminCredentials).body.get()
            savedMovies.size shouldBe 0
            jsonString(savedMovies) shouldBe "[ ]"
        }

        "should respond with UNAUTHORIZED when saving a movie with incorrect credentials" {
            // When
            val exception = shouldThrow<HttpClientResponseException>
            { createNewMovie(newMovieRequest(100), nonAdminCredentials) }

            // Then
            exception.message shouldBe "Unauthorized"
            exception.status shouldBe HttpStatus.UNAUTHORIZED
        }

        "should respond with UNAUTHORIZED when getting movie with incorrect credentials" {
            // When
            val exception = shouldThrow<HttpClientResponseException> { getAllMoviesWithAuth(nonAdminCredentials) }

            // Then
            exception.message shouldBe "Unauthorized"
            exception.status shouldBe HttpStatus.UNAUTHORIZED
        }
    }
}
