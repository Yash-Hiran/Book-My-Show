package com.demo.book.api

import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import com.demo.authentication.userCredentials.service.AuthenticationService
import com.demo.book.BaseIntegrationSpec
import com.demo.book.movie.entity.Movie
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.movie.service.MovieService
import com.demo.book.utils.get
import com.demo.book.utils.post
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import io.micronaut.http.BasicAuth
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import norm.executeCommand
import javax.sql.DataSource

@MicronautTest
class MovieApiTest(override var dataSource: DataSource) : BaseIntegrationSpec() {
    private val authenticationRepository = AuthenticationRepository(dataSource)
    private val authenticationService = AuthenticationService(authenticationRepository)
    private val movieRepository = MovieRepository(dataSource)
    private val movieService = MovieService(movieRepository)
    private val movieApi = MovieApi(movieService, dataSource)

    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        clearData()
    }

    override fun clearData(): Unit = dataSource.connection.use{
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE movies RESTART IDENTITY CASCADE;")
    }

    private fun createUser(basicAuth: BasicAuth) = dataSource.connection.use {
        it.executeCommand(
            """
            | INSERT INTO users (username, password)
            | VALUES ('${basicAuth.username}', CRYPT('${basicAuth.password}', GEN_SALT('bf')));
            | """.trimMargin().trimIndent()
        )
    }

    init {
        "should save movie with correct credentials" {
            // Given
            createUser(BasicAuth("kraken", "ryu"))
            val credentials = BasicAuth("kraken", "ryu")

            // When
            val response = createNewMovie(newMovieRequest(100), credentials)

            // Then
            response.status shouldBe HttpStatus.OK
            response.body.get() shouldBe 1
        }

        "should get all saved movies" {
            // When
            val credentials = BasicAuth("kraken", "ryu")
            createUser(credentials)
            createNewMovie(newMovieRequest(100), credentials)
            val response = httpClient.get<List<Movie>>("/movies")

            // Then
            response.status shouldBe HttpStatus.OK
            val savedMovies = response.body.get()
            savedMovies.size shouldBe 1
            jsonString(savedMovies[0]) shouldBe """
                |{
                |  "id" : 1,
                |  "title" : "Avengers",
                |  "duration" : 100
                |}
            """.trimMargin().trimIndent()
        }

        "should get empty list if no movies are present" {
            // When
            val response = httpClient.get<List<Movie>>("/movies")

            // Then
            response.status shouldBe HttpStatus.OK
            val savedMovies = response.body.get()
            savedMovies.size shouldBe 0
            jsonString(savedMovies) shouldBe "[ ]"
        }

        "should throw exception when saving a movie with incorrect credentials" {
            // Given
            createUser(BasicAuth("kraken", "ryu"))
            val credentials = BasicAuth("blackbeard", "nio")

            // When
            val exception = shouldThrow<InvalidUsernameOrPasswordException>
            { createNewMovie(newMovieRequest(100), credentials) }

            // Then
            exception.code shouldBe "com.authentication.service"
            exception.message shouldBe "Invalid username or password"
        }
    }

    private fun createNewMovie(avengersMovie: CreateMovieRequest, basicAuth: BasicAuth): HttpResponse<Int> {
        return movieApi.saveMovie(basicAuth, avengersMovie)
    }

    private fun newMovieRequest(duration: Int): CreateMovieRequest {
        return CreateMovieRequest(
            "Avengers",
            duration
        )
    }
}
