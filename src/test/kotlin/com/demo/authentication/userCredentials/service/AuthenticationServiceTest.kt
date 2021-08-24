package com.demo.authentication.userCredentials.service

import com.demo.authentication.AuthenticationIntegrationSpec
import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.micronaut.http.BasicAuth
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import javax.inject.Inject
import javax.sql.DataSource

@MicronautTest
class AuthenticationServiceTest(@Inject override var dataSource: DataSource) : AuthenticationIntegrationSpec() {
    private val authenticationRepository = AuthenticationRepository(dataSource)
    private val authenticationService = AuthenticationService(authenticationRepository)

    init {
        addPgcryptoExtension()

        "should return true for correct credentials" {
            // given
            createUser(BasicAuth("mihir", "12345"))
            val credentials = BasicAuth("mihir", "12345")

            // when
            val result = authenticationService.checkCredentials(credentials)

            // then
            result shouldBe true
        }

        "should throw exception for incorrect credentials" {
            // given
            createUser(BasicAuth("yash", "asdf"))
            val credentials = BasicAuth("mihir", "12345")

            // when
            val exception = shouldThrow<InvalidUsernameOrPasswordException>
            { authenticationService.checkCredentials(credentials) }

            // then
            exception.message shouldBe "Invalid username or password"
            exception.code shouldBe "com.authentication.service"
        }
    }
}
