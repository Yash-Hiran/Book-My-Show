package com.demo.authentication.userCredentials.repository

import com.demo.authentication.AuthenticationIntegrationSpec
import com.demo.authentication.userCredentials.request.CredentialRequest
import io.kotest.matchers.shouldBe
import norm.executeCommand

class AuthenticationRepositoryTest : AuthenticationIntegrationSpec() {
    private val authenticationRepository = AuthenticationRepository(dataSource)

    init {
        "should return true for correct credentials" {
            // given
            val credentials = CredentialRequest("mihir", "12345")
            createUser("mihir", "12345")

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe true
        }

        "should return false for incorrect credentials" {
            // given
            val credentials = CredentialRequest("yash", "asdf")
            createUser("mihir", "12345")

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe false
        }
    }

    private fun  createUser(username: String, password: String) = dataSource.connection.use { it.executeCommand("""
        |INSERT INTO users(username, password)
        |VALUES ('$username', CRYPT('$password', GEN_SALT('bf')));
        |""".trimMargin().trimIndent())
    }
}
