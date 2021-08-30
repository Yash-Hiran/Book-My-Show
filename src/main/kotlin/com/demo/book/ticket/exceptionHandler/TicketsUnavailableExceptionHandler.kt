package com.demo.book.ticket.exceptionHandler

import com.demo.ApiError
import com.demo.book.ticket.exception.TicketsUnavailableException
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
    Requires(classes = [TicketsUnavailableException::class, ExceptionHandler::class])
)
class TicketsUnavailableExceptionHandler : ExceptionHandler<TicketsUnavailableException, HttpResponse<ApiError>> {
    override fun handle(request: HttpRequest<*>?, exception: TicketsUnavailableException): HttpResponse<ApiError> =
        HttpResponse.badRequest(ApiError(exception.code, "No tickets are available for this show"))
}
