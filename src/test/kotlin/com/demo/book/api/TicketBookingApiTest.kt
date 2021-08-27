package com.demo.book.api

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BookingIntegrationSpec
import com.demo.book.ticket.entity.Ticket
import com.demo.book.ticket.request.TicketRequest
import com.demo.utils.getWithBasicAuth
import com.demo.utils.postWithBasicAuth
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
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

        "should save a ticket with admin credentials" {
            // Given
            val showDate = LocalDate.now().plusDays(1).toString()
            val startTime = LocalDateTime.now().plusDays(1).toString()
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(120), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest(showDate, startTime), adminCredentials)

            // When
            val response = bookTicketWithAuth(createTicketRequest(1, 1, 1234567890), adminCredentials)

            // Then
            response.status shouldBe HttpStatus.OK
            response.body() shouldBe TicketRequest(1, 1, 1234567890)
        }
    }

    private fun createTicketRequest(showId: Int, seatNo: Int, phoneNo: Int) = TicketRequest(showId, seatNo, phoneNo)

    private fun bookTicketWithAuth(ticketRequest: TicketRequest, credentials: UserCredentialsRequest) =
        httpClient.postWithBasicAuth(
            url = "/tickets",
            body = ticketRequest,
            userCredentialsRequest = credentials
        )
}
