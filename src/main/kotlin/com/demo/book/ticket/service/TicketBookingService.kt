package com.demo.book.ticket.service

import com.demo.book.show.entity.Show
import com.demo.book.show.exception.ShowNotAvailableForReservationException
import com.demo.book.ticket.entity.Ticket
import com.demo.book.ticket.exception.TicketsUnavailableException
import com.demo.book.ticket.repository.TicketBookingRepository
import com.demo.book.ticket.request.TicketRequest
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketBookingService(@Inject private val ticketBookingRepository: TicketBookingRepository) {
    private fun Show.isNotWithinTimeLimitOf(days: Long) = showDate > LocalDate.now().plusDays(days)

    fun saveTicket(ticketRequest: TicketRequest): Ticket {
        val show = ticketBookingRepository.getShowById(ticketRequest.showId)
        if (show.isNotWithinTimeLimitOf(7))
            throw ShowNotAvailableForReservationException("com.book.api.service")
        val seatNo = ticketBookingRepository.getAvailableSeatNo(ticketRequest.showId)
        if (seatNo > show.capacity)
            throw TicketsUnavailableException("com.book.api.service")
        return ticketBookingRepository.saveTicket(ticketRequest, seatNo)
    }
}
