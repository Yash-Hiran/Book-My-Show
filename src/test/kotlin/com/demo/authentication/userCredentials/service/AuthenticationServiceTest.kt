package com.demo.authentication.userCredentials.service

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@MicronautTest
class AuthenticationServiceTest() : StringSpec() {
    private val mockAuthenticationRepository = mockk<AuthenticationRepository>()
    private val authenticationService = AuthenticationService(mockAuthenticationRepository)

    override fun isolationMode() = IsolationMode.InstancePerTest

    init {
        "should return true for correct credentials" {
            // given
            val credentials = UserCredentialsRequest("mihir", "12345")

            // when
            every { mockAuthenticationRepository.checkCredentials(credentials) } returns true
            val result = authenticationService.checkCredentials(credentials)

            // then
            verify(exactly = 1) { mockAuthenticationRepository.checkCredentials(credentials) }
            result shouldBe true
        }

        "should return false for incorrect credentials" {
            val credentials = UserCredentialsRequest("mihir", "12345")

            // when
            every { mockAuthenticationRepository.checkCredentials(credentials) } returns false
            val result = authenticationService.checkCredentials(credentials)

            // then
            verify(exactly = 1) { mockAuthenticationRepository.checkCredentials(credentials) }
            result shouldBe false
        }
    }
}
