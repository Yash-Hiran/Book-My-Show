package com.demo.authentication.exceptionHandler

import com.demo.ApiError
import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import javax.inject.Singleton

@Produces
@Singleton
@Requirements(
    Requires(classes = [InvalidUsernameOrPasswordException::class, ExceptionHandler::class])
)
class InvalidUsernameOrPasswordHandler : ExceptionHandler<InvalidUsernameOrPasswordException, HttpResponse<ApiError>> {
    override fun handle(
        request: HttpRequest<*>?,
        exception: InvalidUsernameOrPasswordException
    ):
            HttpResponse<ApiError> {
        return HttpResponse.badRequest(ApiError(exception.code, "Invalid username or password"))
    }
}
