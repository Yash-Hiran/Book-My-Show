package com.demo.authentication

import com.demo.authentication.userCredentials.UserCredentialsRequest
import com.demo.authentication.userCredentials.service.AuthenticationService
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminUserAuthenticationProvider(@Inject private val authenticationService: AuthenticationService) :
    AuthenticationProvider {
    override fun authenticate(httpRequest: HttpRequest<*>?, authenticationRequest: AuthenticationRequest<*, *>?):
            Publisher<AuthenticationResponse> {
        if (authenticationRequest != null && authenticationRequest.identity != null &&
            authenticationRequest.secret != null
        ) {
            val userCredentialsRequest = UserCredentialsRequest(
                    authenticationRequest.identity as String,
                    authenticationRequest.secret as String
                )
            if (authenticationService.checkCredentials(userCredentialsRequest)) {
                return Flowable.just(UserDetails(authenticationRequest.identity as String, listOf()))
            }
        }
        return Flowable.just(AuthenticationFailed())
    }
}
