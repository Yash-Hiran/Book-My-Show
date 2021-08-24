package com.demo.authentication.userCredentials.service

import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import io.micronaut.http.BasicAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService(@Inject private val authenticationRepository: AuthenticationRepository) {
    fun checkCredentials(basicAuth: BasicAuth): Boolean {
        val result = authenticationRepository.checkCredentials(basicAuth)
        return if (result) true else throw InvalidUsernameOrPasswordException("com.authentication.service")
    }
}
