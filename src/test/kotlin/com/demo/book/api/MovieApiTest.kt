package com.demo.book.api

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BaseIntegrationSpec
import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.utils.getWithBasicAuth
import com.demo.utils.postWithBasicAuth
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import norm.executeCommand
import javax.sql.DataSource

@MicronautTest
class MovieApiTest(override var dataSource: DataSource) : BaseIntegrationSpec() {

    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        clearData()
    }

    override fun clearData(): Unit = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE movies RESTART IDENTITY CASCADE;")
    }

    private fun createUser(userCredentialsRequest: UserCredentialsRequest) = dataSource.connection.use {
        it.executeCommand(
            """
            | INSERT INTO users (username, password)
            | VALUES ('${userCredentialsRequest.username}', CRYPT('${userCredentialsRequest.password}', GEN_SALT('bf')));
            | """.trimMargin().trimIndent()
        )
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

        "should respond with bad request when saving a movie with incorrect credentials" {
            // When
            val exception = shouldThrow<HttpClientResponseException> { createNewMovie(newMovieRequest(100), nonAdminCredentials) }

            // Then
            exception.message shouldBe "Unauthorized"
            exception.status shouldBe HttpStatus.UNAUTHORIZED
        }

        "should respond with bad request when getting movie with incorrect credentials" {
            // When
            val exception = shouldThrow<HttpClientResponseException> { getAllMoviesWithAuth(nonAdminCredentials) }

            // Then
            exception.message shouldBe "Unauthorized"
            exception.status shouldBe HttpStatus.UNAUTHORIZED
        }
    }

    private fun getAllMoviesWithAuth(userCredentialsRequest: UserCredentialsRequest) =
        httpClient.getWithBasicAuth<List<Movie>>(
            url = "/movies",
            userCredentialsRequest = userCredentialsRequest
        )

    private fun createNewMovie(
        avengersMovie: CreateMovieRequest,
        userCredentialsRequest: UserCredentialsRequest
    ) = httpClient.postWithBasicAuth(
        url = "/movies",
        body = jsonMapper.writeValueAsString(avengersMovie),
        userCredentialsRequest = userCredentialsRequest
    )

    private fun newMovieRequest(duration: Int) =
        CreateMovieRequest(
            "Avengers",
            duration
        )
}
