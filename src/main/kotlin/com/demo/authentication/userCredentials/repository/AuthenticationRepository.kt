package com.demo.authentication.userCredentials.repository

import authentication.CheckUserParams
import authentication.CheckUserQuery
import com.demo.authentication.userCredentials.request.CredentialRequest
import norm.query
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class AuthenticationRepository(@Inject private val dataSource: DataSource) {
    fun checkCredentials(credentials: CredentialRequest) =
        CheckUserQuery()
            .query(dataSource.connection, CheckUserParams(credentials.username, credentials.password))
            .isNotEmpty()
}
