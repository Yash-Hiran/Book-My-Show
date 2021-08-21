package com.demo.authentication.userCredentials.service

import authentication.CheckUserQuery
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import io.micronaut.http.BasicAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService(@Inject private val authenticationRepository: AuthenticationRepository) {
    fun checkCredentials(basicAuth: BasicAuth): Boolean {
        return authenticationRepository.checkCredentials(basicAuth)
    }
}
