package com.demo.authentication

import com.demo.IntegrationSpec
import io.micronaut.http.BasicAuth
import norm.executeCommand

open class AuthenticationIntegrationSpec : IntegrationSpec() {
    override fun clearData() = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE users RESTART IDENTITY")
    }

    protected fun createUser(basicAuth: BasicAuth) = dataSource.connection.use {
        it.executeCommand("""
            | INSERT INTO users (username, password)
            | VALUES ('${basicAuth.username}', CRYPT('${basicAuth.password}', GEN_SALT('bf')));
            | """.trimMargin().trimIndent()
        )
    }

    protected fun addPgcryptoExtension() = dataSource.connection.use {
        it.executeCommand("CREATE EXTENSION IF NOT EXISTS pgcrypto;")
    }
}
