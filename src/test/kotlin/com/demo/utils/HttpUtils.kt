package com.demo.utils

import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient

inline fun <reified T> HttpClient.getWithBasicAuth(
    url: String,
    userCredentialsRequest: UserCredentialsRequest
) = toBlocking().exchange(
    HttpRequest.GET<T>(url).basicAuth(
        userCredentialsRequest.username,
        userCredentialsRequest.password
    ),
    T::class.java
)

inline fun <reified T> HttpClient.postWithBasicAuth(
    url: String,
    body: T,
    userCredentialsRequest: UserCredentialsRequest
) = toBlocking()
    .exchange(
        HttpRequest.POST(url, body).basicAuth(
            userCredentialsRequest.username,
            userCredentialsRequest.password
        ),
        T::class.java
    )
