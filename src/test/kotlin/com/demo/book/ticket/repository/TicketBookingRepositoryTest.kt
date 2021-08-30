package com.demo.book.ticket.repository

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BookingIntegrationSpec
import com.demo.book.ticket.request.TicketRequest
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.sql.DataSource

class TicketBookingRepositoryTest(@Inject override var dataSource: DataSource) :
    BookingIntegrationSpec() {
    @Inject
    val ticketBookingRepository = TicketBookingRepository(dataSource)

    init {
        val adminCredentials = UserCredentialsRequest("abc", "def")

        "should save a ticket for a show" {
            // Given
            val startDate = LocalDate.now().plusDays(1).toString()
            val startTime = LocalDateTime.now().plusDays(1).toString()
            val ticketRequest = createTicketRequest(1, 1234567890)
            val seatNo = 1
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(200), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest(startDate, startTime, 100), adminCredentials)

            // When
            val ticket = ticketBookingRepository.saveTicket(ticketRequest, seatNo)

            // Then
            val savedTicket = ticketBookingRepository.getTicketById(1)
            ticket shouldBe savedTicket
        }
    }

    private fun createTicketRequest(showId: Int, phoneNo: Int) = TicketRequest(showId, phoneNo)
}
