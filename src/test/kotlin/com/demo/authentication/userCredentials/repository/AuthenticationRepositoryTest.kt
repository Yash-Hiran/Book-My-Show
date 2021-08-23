package com.demo.authentication.userCredentials.repository

import com.demo.authentication.userCredentials.request.CredentialRequest
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import norm.executeCommand
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import javax.inject.Inject
import javax.sql.DataSource

@MicronautTest
class AuthenticationRepositoryTest(@Inject private val dataSource: DataSource) : StringSpec() {
    private val authenticationRepository = AuthenticationRepository(dataSource)
    override fun afterEach(testCase: TestCase, result: TestResult) {
        super.afterEach(testCase, result)
        clearData()
    }
    private fun clearData() = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY;")
    }

    init {
        addPgcrypto()
        "should return true for correct credentials" {
            // given
            val credentials = CredentialRequest("mihir", "12345")
            createUser()

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe true
        }

        "should return false for incorrect credentials" {
            // given
            val credentials = CredentialRequest("raj", "xyz")
            createUser()

            // when
            val result = authenticationRepository.checkCredentials(credentials)

            // then
            result shouldBe false
        }
    }

    private fun addPgcrypto() = dataSource.connection.use {
        it.executeCommand("CREATE EXTENSION pgcrypto;")
    }

    private fun createUser() = dataSource.connection.use {
        it.executeCommand("""insert into users (username, password)
            | values ('mihir', CRYPT('12345', gen_salt('bf')));
            | """.trimMargin())
    }
}
