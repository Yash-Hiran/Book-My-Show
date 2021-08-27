package com.demo.book.show.exceptionhandler

import com.demo.ApiError
import com.demo.book.show.exception.ShowDoesNotExistException
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
    Requires(classes = [ShowDoesNotExistException::class, ExceptionHandler::class])
)
class ShowDoesNotExistExceptionHandler : ExceptionHandler<ShowDoesNotExistException, HttpResponse<ApiError>> {
    override fun handle(request: HttpRequest<*>?, exception: ShowDoesNotExistException): HttpResponse<ApiError> {
        return HttpResponse.badRequest(ApiError(exception.code, "Invalid show ID. This show does not exist."))
    }
}
