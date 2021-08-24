package com.demo.authentication.userCredentials.service

import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import com.demo.authentication.userCredentials.request.CredentialRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService(@Inject private val authenticationRepository: AuthenticationRepository) {
    fun checkCredentials(CredentialRequest: CredentialRequest): Boolean {
        val result = authenticationRepository.checkCredentials(CredentialRequest)
        return if (result) true else throw InvalidUsernameOrPasswordException("com.authentication.service")
    }
}
