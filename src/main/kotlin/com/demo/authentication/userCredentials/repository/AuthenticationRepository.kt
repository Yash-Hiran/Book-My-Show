package com.demo.authentication.userCredentials.repository

import authentication.CheckCredentialsParams
import authentication.CheckCredentialsQuery
import io.micronaut.http.BasicAuth
import norm.query
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class AuthenticationRepository(@Inject private val dataSource: DataSource) {
    fun checkCredentials(basicAuth: BasicAuth) =
        CheckCredentialsQuery()
            .query(dataSource.connection, CheckCredentialsParams(basicAuth.username, basicAuth.password))
            .isNotEmpty()
}
