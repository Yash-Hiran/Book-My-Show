package com.demo.authentication.userCredentials.service

import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import com.demo.authentication.userCredentials.UserCredentialsRequest
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService(@Inject private val authenticationRepository: AuthenticationRepository) {
    fun checkCredentials(userCredentialsRequest: UserCredentialsRequest): Boolean {
        val result = authenticationRepository.checkCredentials(userCredentialsRequest)
        return if (result) true else throw InvalidUsernameOrPasswordException("com.authentication.service")
    }
}
