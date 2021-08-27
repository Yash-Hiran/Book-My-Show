package com.demo.book.ticket.service

import com.demo.book.ticket.entity.Ticket
import com.demo.book.ticket.repository.TicketBookingRepository
import com.demo.book.ticket.request.TicketRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketBookingService(@Inject private val ticketBookingRepository: TicketBookingRepository) {
    fun saveTicket(ticketRequest: TicketRequest) = ticketBookingRepository.saveTicket(ticketRequest)
}
