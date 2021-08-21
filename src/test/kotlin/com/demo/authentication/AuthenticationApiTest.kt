package com.demo.authentication

import com.demo.book.BaseIntegrationSpec
import com.demo.book.utils.post
import io.kotest.data.Headers2
import io.kotest.data.headers
import io.kotest.matchers.shouldBe
import io.micronaut.http.BasicAuth
import io.micronaut.http.HttpResponse


class AuthenticationApiTest : BaseIntegrationSpec() {
    init {
        "should successfully authenticate with valid credentials" {
            tryLogin("mihir", "asdfg")
        }
    }

    private fun tryLogin(username: String, password: String): HttpResponse<Headers2> {
        return httpClient.post("/login", headers(username, password))
    }
}
