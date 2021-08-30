package com.demo.book

import com.demo.IntegrationSpec
import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.show.entity.Show
import com.demo.book.show.request.CreateShowRequest
import com.demo.utils.getWithBasicAuth
import com.demo.utils.postWithBasicAuth
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.test.TestCase
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import norm.executeCommand
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest
abstract class BookingIntegrationSpec : IntegrationSpec() {

    @Inject
    @field:Client("/api")
    protected lateinit var httpClient: HttpClient

    protected val jsonMapper: ObjectMapper = jacksonObjectMapper().also {
        it.enable(SerializationFeature.INDENT_OUTPUT)
        it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        it.dateFormat = (DateFormat.getDateTimeInstance())
        it.registerModule(JavaTimeModule())
    }

    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        clearData()
    }

    protected fun jsonString(movie: Any?): String = jsonMapper.writeValueAsString(movie)

    protected fun createNewShowWithBasicAuth(
        show: CreateShowRequest,
        userCredentialsRequest: UserCredentialsRequest
    ): HttpResponse<String> =
        httpClient.postWithBasicAuth(
            url = "/shows",
            body = jsonString(show),
            userCredentialsRequest = userCredentialsRequest
        )

    protected fun getAllMoviesWithAuth(userCredentialsRequest: UserCredentialsRequest): HttpResponse<List<Movie>> =
        httpClient.getWithBasicAuth<List<Movie>>(
            url = "/movies",
            userCredentialsRequest = userCredentialsRequest
        )

    protected fun getAllShowsWithAuth(userCredentialsRequest: UserCredentialsRequest) =
        httpClient.getWithBasicAuth<Map<String, List<Show>>>("/shows", userCredentialsRequest)

    protected fun newShowRequest(startDate: String, startTime: String) = CreateShowRequest(
        1,
        LocalDate.parse(startDate),
        LocalDateTime.parse(startTime)
    )

    protected fun getAvailableShowsWithAuth(userCredentialsRequest: UserCredentialsRequest) =
        httpClient.getWithBasicAuth<List<Int>>("/shows/1/seats", userCredentialsRequest)

    protected fun createNewMovie(
        avengersMovie: CreateMovieRequest,
        userCredentialsRequest: UserCredentialsRequest
    ): HttpResponse<String> =
        httpClient.postWithBasicAuth(
            url = "/movies",
            body = jsonString(avengersMovie),
            userCredentialsRequest = userCredentialsRequest
        )

    protected fun newMovieRequest(duration: Int) =
        CreateMovieRequest(
            "Avengers",
            duration
        )

    protected fun createUser(userCredentialsRequest: UserCredentialsRequest) = dataSource.connection.use {
        it.executeCommand(
            """
            | INSERT INTO users (username, password)
            | VALUES ('${userCredentialsRequest.username}', CRYPT('${userCredentialsRequest.password}', GEN_SALT('bf')));
            | """.trimMargin().trimIndent()
        )
    }
}
