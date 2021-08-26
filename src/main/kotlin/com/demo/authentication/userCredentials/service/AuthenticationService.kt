package com.demo.authentication.userCredentials.service

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService(@Inject private val authenticationRepository: AuthenticationRepository) {
    fun checkCredentials(userCredentialsRequest: UserCredentialsRequest) = authenticationRepository.checkCredentials(userCredentialsRequest)
}
