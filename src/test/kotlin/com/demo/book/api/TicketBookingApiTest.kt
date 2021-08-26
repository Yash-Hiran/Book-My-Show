package com.demo.book.api

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BookingIntegrationSpec
import com.demo.book.ticket.request.TicketRequest
import com.demo.book.utils.post
import com.demo.utils.postWithBasicAuth
import io.kotest.assertions.show.show
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import norm.executeCommand
import java.net.http.HttpClient
import java.net.http.HttpResponse

class TicketBookingApiTest : BookingIntegrationSpec() {
    override fun clearData() = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE customers RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE tickets RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE movies RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE shows RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY CASCADE;")
    }

    init {
        val adminCredentials = UserCredentialsRequest("abc", "def")
        val nonAdminCredentials = UserCredentialsRequest("uvw", "xyz")

        "should book ticket with admin credentials" {
            // Given
            val showDate = "2021-09-25"
            val startTime = "2021-09-25T15:50:36.0680763"
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(120), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest(showDate, startTime), adminCredentials)
            createCustomer("john doe")

            // When
            val response = bookTicketWithAuth(createTicketRequest(1, 1), adminCredentials)

            // Then
            response.status.code shouldBe HttpStatus.OK
            response.body.get() shouldBe "1"
        }
    }

    private fun createCustomer(name: String) = dataSource.connection.use {
        it.executeCommand("INSERT INTO customers (name) values ('$name');")
    }

    private fun createTicketRequest(showId: Int, customerId: Int) = TicketRequest(showId, customerId)

    private fun bookTicketWithAuth(ticketRequest: TicketRequest, credentials: UserCredentialsRequest) =
        httpClient.postWithBasicAuth(
            url = "/book",
            body = ticketRequest,
            userCredentialsRequest = credentials
        )
}
