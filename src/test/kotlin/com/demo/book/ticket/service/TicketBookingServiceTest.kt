package com.demo.book.ticket.service

import com.demo.IsolatedTestSpec
import com.demo.book.show.entity.Show
import com.demo.book.show.exception.ShowNotAvailableForReservationException
import com.demo.book.ticket.entity.Ticket
import com.demo.book.ticket.exception.TicketsUnavailableException
import com.demo.book.ticket.repository.TicketBookingRepository
import com.demo.book.ticket.request.TicketRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime

class TicketBookingServiceTest() : IsolatedTestSpec() {
    companion object {
        private val CURRENT_DATE = LocalDate.now()
        private val START_TIME = LocalDateTime.now()
        private val END_TIME = START_TIME.plusHours(2)
        private const val DEFAULT_CAPACITY = 100
        private const val NO_CAPACITY = 0
    }

    private val mockTicketBookingRepository = mockk<TicketBookingRepository>()
    private val ticketBookingService = TicketBookingService(mockTicketBookingRepository)
    private val ticketRequest = createTicketRequest(1, 1234567890)

    init {
        "should save ticket for show within next 7 days & show is available" {
            // Given
            val show = createShow(CURRENT_DATE, START_TIME, END_TIME, DEFAULT_CAPACITY)
            every { mockTicketBookingRepository.getShowById(any()) }.returns(show)
            every { mockTicketBookingRepository.getAvailableSeatNo(any()) }.returns(1)
            every { mockTicketBookingRepository.saveTicket(any(), any()) }.returns(
                Ticket(1, 1, 1, 1234567890)
            )

            // When
            val ticket = ticketBookingService.saveTicket(ticketRequest)

            // Then
            ticket shouldBe Ticket(1, 1, 1, 1234567890)
            verify(exactly = 1) { mockTicketBookingRepository.saveTicket(ticketRequest, 1) }
        }

        "should throw exception when trying to book ticket after 7 days from now" {
            // Given
            val show = createShow(
                CURRENT_DATE.plusDays(8), START_TIME.plusDays(8), END_TIME, DEFAULT_CAPACITY
            )
            every { mockTicketBookingRepository.getShowById(any()) }.returns(show)

            // When
            val exception =
                shouldThrow<ShowNotAvailableForReservationException> { ticketBookingService.saveTicket(ticketRequest) }

            // Then
            exception.code shouldBe "com.book.api.service"
            verify(exactly = 0) { mockTicketBookingRepository.saveTicket(ticketRequest, 1) }
        }

        "should throw exception when trying to book ticket when no tickets are available" {
            // Given
            val show = createShow(CURRENT_DATE, START_TIME, END_TIME, NO_CAPACITY)
            every { mockTicketBookingRepository.getShowById(any()) }.returns(show)
            every { mockTicketBookingRepository.getAvailableSeatNo(any()) }.returns(1)

            // When
            val exception =
                shouldThrow<TicketsUnavailableException> { ticketBookingService.saveTicket(ticketRequest) }

            // Then
            exception.code shouldBe "com.book.api.service"
            verify(exactly = 0) { mockTicketBookingRepository.saveTicket(ticketRequest, 1) }
        }
    }

    private fun createTicketRequest(showId: Int, phoneNo: Int) = TicketRequest(showId, phoneNo)

    private fun createShow(date: LocalDate, startTime: LocalDateTime, endTime: LocalDateTime, capacity: Int) = Show(
        1,
        1,
        date,
        startTime,
        endTime,
        capacity
    )
}
