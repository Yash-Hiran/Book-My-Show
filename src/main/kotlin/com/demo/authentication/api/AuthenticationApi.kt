package com.demo.authentication.api

import com.demo.authentication.userCredentials.request.CredentialRequest
import com.demo.authentication.userCredentials.service.AuthenticationService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import javax.inject.Inject

@Controller
class AuthenticationApi(@Inject private val authenticationService: AuthenticationService) {
    @Post("/login")
    fun login(@Body credentialRequest: CredentialRequest): HttpResponse<Boolean> =
        HttpResponse.ok(authenticationService.checkCredentials(credentialRequest))
}
