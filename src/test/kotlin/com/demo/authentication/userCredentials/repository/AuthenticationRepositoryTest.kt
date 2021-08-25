package com.demo.authentication.userCredentials.repository

import com.demo.authentication.AuthenticationIntegrationSpec
import com.demo.authentication.userCredentials.UserCredentialsRequest
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import javax.inject.Inject
import javax.sql.DataSource

@MicronautTest
class AuthenticationRepositoryTest(@Inject override var dataSource: DataSource) : AuthenticationIntegrationSpec() {
    private val authenticationRepository = AuthenticationRepository(dataSource)

    init {
        addPgcryptoExtension()

        "should return true for correct credentials" {
            // given
            createUser(UserCredentialsRequest("mihir", "12345"))
            val credentials = UserCredentialsRequest("mihir", "12345")

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe true
        }

        "should return false for incorrect credentials" {
            // given
            createUser(UserCredentialsRequest("yash", "zcv"))
            val credentials = UserCredentialsRequest("raj", "xyz")

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe false
        }
    }
}
