package com.demo.book.ticket.service

import com.demo.book.show.exception.ShowNotAvailableException
import com.demo.book.ticket.entity.Ticket
import com.demo.book.ticket.repository.TicketBookingRepository
import com.demo.book.ticket.request.TicketRequest
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketBookingService(@Inject private val ticketBookingRepository: TicketBookingRepository) {
    fun saveTicket(ticketRequest: TicketRequest): Ticket {
        val timeLimit = LocalDateTime.now().plusDays(7)
        val show = ticketBookingRepository.getShowById(ticketRequest.showId)
        if (show.startTime > timeLimit)
            throw ShowNotAvailableException("com.book.api.service")
        val seatNo = ticketBookingRepository.getAvailableSeatNo(ticketRequest.showId)
        return ticketBookingRepository.saveTicket(ticketRequest, seatNo)
    }
}
