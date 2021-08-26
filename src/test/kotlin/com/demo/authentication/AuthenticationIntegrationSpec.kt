package com.demo.authentication

import com.demo.IntegrationSpec
import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import norm.executeCommand

open class AuthenticationIntegrationSpec : IntegrationSpec() {
    override fun clearData() = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY CASCADE;")
    }

    protected fun createUser(userCredentialsRequest: UserCredentialsRequest) = dataSource.connection.use {
        it.executeCommand("""
            | INSERT INTO users (username, password)
            | VALUES ('${userCredentialsRequest.username}', CRYPT('${userCredentialsRequest.password}', GEN_SALT('bf')));
            | """.trimMargin().trimIndent()
        )
    }

    protected fun addPgcryptoExtension() = dataSource.connection.use {
        it.executeCommand("CREATE EXTENSION IF NOT EXISTS pgcrypto;")
    }
}
