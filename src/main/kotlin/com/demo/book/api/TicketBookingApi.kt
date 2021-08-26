package com.demo.book.api

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
class TicketBookingApi {
    @Post("/book")
    fun bookTicket() {
    }
}
