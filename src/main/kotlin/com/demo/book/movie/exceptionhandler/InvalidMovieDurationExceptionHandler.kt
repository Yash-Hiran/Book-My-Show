package com.demo.book.movie.exceptionhandler

import com.demo.book.movie.exception.InvalidMovieDurationException
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
    Requires(classes = [InvalidMovieDurationException::class, ExceptionHandler::class])
)
class InvalidMovieDurationExceptionHandler : ExceptionHandler<InvalidMovieDurationException, HttpResponse<ApiError>> {
    override fun handle(request: HttpRequest<*>?, exception: InvalidMovieDurationException): HttpResponse<ApiError> {
        return HttpResponse.badRequest(ApiError(exception.errorCode, exception.message))
    }
}

data class ApiError(val code: String, val message: String)
