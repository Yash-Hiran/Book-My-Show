package com.demo.authentication

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import norm.executeCommand
import javax.inject.Inject
import javax.sql.DataSource

@MicronautTest
open class AuthenticationIntegrationSpec : StringSpec() {
    @Inject
    @field:Client("/api")
    protected lateinit var httpClient: HttpClient

    @Inject
    protected lateinit var dataSource: DataSource

    protected val jsonMapper: ObjectMapper = jacksonObjectMapper().also {
        it.enable(SerializationFeature.INDENT_OUTPUT)
        it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        clearData()
    }

    protected fun clearData() {
        dataSource.connection.use { connection ->
            connection.executeCommand("TRUNCATE users RESTART IDENTITY CASCADE;")
        }
    }

    protected fun jsonString(credentials: Any?) = jsonMapper.writeValueAsString(credentials)
}
