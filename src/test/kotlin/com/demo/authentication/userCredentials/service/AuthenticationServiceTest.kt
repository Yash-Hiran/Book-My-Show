package com.demo.authentication.userCredentials.service

import com.demo.IsolatedTestSpec
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import com.demo.authentication.userCredentials.request.CredentialRequest
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class AuthenticationServiceTest : IsolatedTestSpec() {
    private val mockAuthenticationRepository = mockk<AuthenticationRepository>()
    private val authenticationService = AuthenticationService(mockAuthenticationRepository)

    init {
        "should return true for correct credentials" {
            // given
            val correctCredentials = CredentialRequest("mihir", "12345")
            every { mockAuthenticationRepository.checkCredentials(any()) } returns true

            // when
            val result = authenticationService.checkCredentials(correctCredentials)

            // then
            verify(exactly = 1) { mockAuthenticationRepository.checkCredentials(correctCredentials) }
            result shouldBe true
        }

        "should return false for incorrect credentials" {
            // given
            val credentials = CredentialRequest("mihir", "12345")
            every { mockAuthenticationRepository.checkCredentials(any()) } returns false

            // when
            val result = authenticationService.checkCredentials(credentials)

            // then
            verify(exactly = 1) { mockAuthenticationRepository.checkCredentials(credentials) }
            result shouldBe false
        }
    }
}
