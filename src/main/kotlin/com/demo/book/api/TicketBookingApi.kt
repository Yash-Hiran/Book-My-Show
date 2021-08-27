package com.demo.book.api

import com.demo.book.ticket.request.TicketRequest
import com.demo.book.ticket.service.TicketBookingService
import io.micronaut.http.HttpResponse.ok
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import javax.inject.Inject

@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
class TicketBookingApi(@Inject private val ticketBookingService: TicketBookingService) {
    @Post("/tickets")
    fun bookTicket(@Body ticketRequest: TicketRequest) = ok(ticketBookingService.saveTicket(ticketRequest))
}
