package com.demo.book.ticket.service

import com.demo.book.ticket.entity.Ticket
import com.demo.book.ticket.exception.TicketIsAlreadyBookedException
import com.demo.book.ticket.repository.TicketBookingRepository
import com.demo.book.ticket.request.TicketRequest
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketBookingService(@Inject private val ticketBookingRepository: TicketBookingRepository) {
    fun saveTicket(ticketRequest: TicketRequest): Ticket {
        if(ticketBookingRepository.isTicketBooked(ticketRequest)){
            throw TicketIsAlreadyBookedException("com.book.api.ticket.service")
        }
        return ticketBookingRepository.saveTicket(ticketRequest)
    }
}
