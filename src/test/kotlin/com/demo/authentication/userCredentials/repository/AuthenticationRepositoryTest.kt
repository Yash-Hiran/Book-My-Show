package com.demo.authentication.userCredentials.repository

import com.demo.authentication.AuthenticationIntegrationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.BasicAuth
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
            createUser(BasicAuth("mihir", "12345"))
            val credentials = BasicAuth("mihir", "12345")

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe true
        }

        "should return false for incorrect credentials" {
            // given
            createUser(BasicAuth("yash", "zcv"))
            val credentials = BasicAuth("raj", "xyz")

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe false
        }
    }
}
