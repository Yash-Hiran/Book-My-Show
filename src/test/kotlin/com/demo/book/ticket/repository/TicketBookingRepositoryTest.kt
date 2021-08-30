package com.demo.book.ticket.repository

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.book.BookingIntegrationSpec
import com.demo.book.show.entity.Show
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

        "should return next available seat" {
            val startDate = LocalDate.now().plusDays(1).toString()
            val startTime = LocalDateTime.now().plusDays(1).toString()
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(200), adminCredentials)
            createNewShowWithBasicAuth(newShowRequest(startDate, startTime, 100), adminCredentials)
            val availableSeat = ticketBookingRepository.getAvailableSeatNo(1)
            availableSeat shouldBe 1
        }

        "should return show by its id" {
            val startDate = LocalDate.now().plusDays(1)
            val startTime = LocalDateTime.now().plusDays(1)
            val endTime = startTime.plusMinutes(200)
            createUser(adminCredentials)
            createNewMovie(newMovieRequest(200), adminCredentials)
            createNewShowWithBasicAuth(
                newShowRequest(startDate.toString(), startTime.toString(), 100),
                adminCredentials
            )
            val show = ticketBookingRepository.getShowById(1)
            show shouldBe Show(1, 1, startDate, startTime, endTime, 100)
        }
    }

    private fun createTicketRequest(showId: Int, phoneNo: Int) = TicketRequest(showId, phoneNo)
}
