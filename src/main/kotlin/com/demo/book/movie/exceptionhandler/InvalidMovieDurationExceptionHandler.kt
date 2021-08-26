package com.demo.book.movie.exceptionhandler

import com.demo.book.movie.exception.InvalidMovieDetailsException
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
    Requires(classes = [InvalidMovieDetailsException::class, ExceptionHandler::class])
)
class InvalidMovieDurationExceptionHandler : ExceptionHandler<InvalidMovieDetailsException, HttpResponse<ApiError>> {
    override fun handle(request: HttpRequest<*>?, exception: InvalidMovieDetailsException): HttpResponse<ApiError> {
        return HttpResponse.badRequest(ApiError(exception.errorCode, exception.message))
    }
}

data class ApiError(val code: String, val message: String)
