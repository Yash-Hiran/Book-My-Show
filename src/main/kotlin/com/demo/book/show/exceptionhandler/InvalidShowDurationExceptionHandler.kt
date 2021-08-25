package com.demo.book.show.exceptionhandler

import com.demo.book.movie.exception.InvalidMovieDetailsException
import com.demo.book.movie.exception.InvalidShowDetailsException
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
    Requires(classes = [InvalidShowDetailsException::class, ExceptionHandler::class])
)
class InvalidShowDurationExceptionHandler : ExceptionHandler<InvalidShowDetailsException, HttpResponse<ApiError>> {
    override fun handle(request: HttpRequest<*>?, exception: InvalidShowDetailsException): HttpResponse<ApiError> {
        return HttpResponse.badRequest(ApiError(exception.errorCode, exception.message))
    }
}

data class ApiError(val code: String, val message: String)
