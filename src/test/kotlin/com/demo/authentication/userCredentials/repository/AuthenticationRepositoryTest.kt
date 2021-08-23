package com.demo.authentication.userCredentials.repository

import com.demo.IsolatedTestSpec
import com.demo.authentication.userCredentials.request.CredentialRequest
import com.zaxxer.hikari.util.DriverDataSource
import io.kotest.matchers.shouldBe
import java.util.*

class AuthenticationRepositoryTest : IsolatedTestSpec() {
    private val dataSource = DriverDataSource(
        "jdbc:postgresql://localhost:5432/bmt_db",
        "com.mysql.jdbc.Driver",
        Properties(),
        "postgres",
        ""
    )
    private val authenticationRepository = AuthenticationRepository(dataSource)

    init {
        "should return true for correct credentials" {
            // given
            val credentials = CredentialRequest("mihir", "12345")

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe true
        }

        "should return false for incorrect credentials" {
            // given
            val credentials = CredentialRequest("yash", "asdf")

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe false
        }
    }
}
