package com.demo.book.api

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BookingIntegrationSpec
import com.demo.book.ticket.request.TicketRequest
import com.demo.utils.postWithBasicAuth
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import norm.executeCommand
import java.time.LocalDate
import java.time.LocalDateTime

class TicketBookingApiTest : BookingIntegrationSpec() {
    override fun clearData() = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE tickets RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE movies RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE shows RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY CASCADE;")
    }

    init {
        val adminCredentials = UserCredentialsRequest("abc", "def")
        val nonAdminCredentials = UserCredentialsRequest("uvw", "xyz")

        "should save a ticket with admin credentials for show within next 7 days" {
            // Given
            val showDate = LocalDate.now().plusDays(1).toString()
            val startTime = LocalDateTime.now().plusDays(1).toString()
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(120), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest(showDate, startTime, 100), adminCredentials)

            // When
            val response = bookTicketWithAuth(createTicketRequest(1, 1234567890), adminCredentials)

            // Then
            response.status shouldBe HttpStatus.OK
            response.body() shouldBe TicketRequest(1, 1234567890)
        }

        "should fail to save a ticket with non-admin credentials" {
            // Given
            val showDate = LocalDate.now().plusDays(1).toString()
            val startTime = LocalDateTime.now().plusDays(1).toString()
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(120), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest(showDate, startTime), adminCredentials)

            // When
            val exception = shouldThrow<HttpClientResponseException>
            { bookTicketWithAuth(createTicketRequest(1, 1234567890), nonAdminCredentials) }

            // Then
            exception.status shouldBe HttpStatus.UNAUTHORIZED
            exception.message shouldBe "Unauthorized"
        }
    }

    private fun createTicketRequest(showId: Int, phoneNo: Int) = TicketRequest(showId, phoneNo)

    private fun bookTicketWithAuth(ticketRequest: TicketRequest, credentials: UserCredentialsRequest) =
        httpClient.postWithBasicAuth(
            url = "/tickets",
            body = ticketRequest,
            userCredentialsRequest = credentials
        )
}
