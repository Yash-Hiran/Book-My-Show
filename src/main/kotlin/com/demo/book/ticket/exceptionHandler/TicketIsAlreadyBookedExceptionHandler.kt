package com.demo.book.ticket.exceptionHandler

import com.demo.ApiError
import com.demo.book.ticket.exception.TicketIsAlreadyBookedException
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Produces
@Singleton
@Requirements(
    Requires(classes = [TicketIsAlreadyBookedException::class, ExceptionHandler::class])
)
class TicketIsAlreadyBookedExceptionHandler : ExceptionHandler<TicketIsAlreadyBookedException, HttpResponse<ApiError>> {
    override fun handle(request: HttpRequest<*>?, exception: TicketIsAlreadyBookedException): HttpResponse<ApiError> {
        return HttpResponse.badRequest(ApiError(exception.code, "Ticket is already booked"))
    }
}
