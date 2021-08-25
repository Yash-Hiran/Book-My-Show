package com.demo.authentication.userCredentials.service

import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import com.demo.authentication.userCredentials.UserCredentialsRequest
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import io.kotest.assertions.throwables.shouldThrow
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

        "should throw exception for incorrect credentials" {
            // given
            every { mockAuthenticationRepository.checkCredentials(any()) } returns false
            val credentials = UserCredentialsRequest("yash", "neo")
            // when
            val exception = shouldThrow<InvalidUsernameOrPasswordException>
            { authenticationService.checkCredentials(credentials) }

            // then
            exception.message shouldBe "Invalid username or password"
            exception.code shouldBe "com.authentication.service"
        }
    }
}
