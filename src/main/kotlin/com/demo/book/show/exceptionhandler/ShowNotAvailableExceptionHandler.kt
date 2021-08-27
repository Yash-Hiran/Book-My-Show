package com.demo.book.show.exceptionhandler

import com.demo.ApiError
import com.demo.book.show.exception.ShowNotAvailableException
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
    Requires(classes = [ShowNotAvailableException::class, ExceptionHandler::class])
)
class ShowNotAvailableExceptionHandler : ExceptionHandler<ShowNotAvailableException, HttpResponse<ApiError>> {
    override fun handle(request: HttpRequest<*>?, exception: ShowNotAvailableException): HttpResponse<ApiError> {
        return HttpResponse.badRequest(ApiError(exception.code, "Tickets can only be reserved for the next 7 days"))
    }
}
