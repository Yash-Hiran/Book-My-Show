package com.demo.authentication

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import norm.executeCommand
import javax.inject.Inject
import javax.sql.DataSource

@MicronautTest
abstract class AuthenticationIntegrationSpec : StringSpec() {
    @Inject
    protected lateinit var dataSource: DataSource

    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        clearData()
    }

    protected fun clearData() {
        dataSource.connection.use { connection ->
            connection.executeCommand("TRUNCATE users RESTART IDENTITY CASCADE;")
        }
    }
}
