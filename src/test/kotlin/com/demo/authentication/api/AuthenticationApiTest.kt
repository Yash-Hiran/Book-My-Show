package com.demo.authentication.api

import com.demo.authentication.AuthenticationIntegrationSpec
import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import com.demo.authentication.userCredentials.request.CredentialRequest
import com.demo.authentication.userCredentials.service.AuthenticationService
import com.demo.book.utils.post
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class AuthenticationApiTest : StringSpec() {
    private val mockAuthenticationService = mockk<AuthenticationService>()
    private val authenticationApi = AuthenticationApi(mockAuthenticationService)

    init {
        "should successfully authenticate with valid credentials" {
            // given
            val credentials = CredentialRequest("mihir", "foobar")
            every { mockAuthenticationService.checkCredentials(any()) } returns true
            val authenticationApi = AuthenticationApi(mockAuthenticationService)

            // when
            val response = authenticationApi.login(credentials)

            // then
            verify(exactly = 1) { mockAuthenticationService.checkCredentials(credentials) }
            response.status shouldBe HttpStatus.OK
            response.body.get() shouldBe true
        }

        "should fail to authenticate with invalid credentials" {
            // given
            val credentials = CredentialRequest("mihir", "bar")
            every { mockAuthenticationService.checkCredentials(any()) } returns false

            // when
            val exception = shouldThrow<InvalidUsernameOrPasswordException> { authenticationApi.login(credentials) }

            // then
            verify(exactly = 1) { mockAuthenticationService.checkCredentials(credentials) }
            exception shouldBe InvalidUsernameOrPasswordException()
            exception.message shouldBe "Error: Invalid username or password"
        }
    }
}
