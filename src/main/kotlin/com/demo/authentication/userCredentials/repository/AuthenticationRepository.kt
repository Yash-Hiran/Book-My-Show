package com.demo.authentication.userCredentials.repository

import authentication.CheckCredentialsParams
import authentication.CheckCredentialsQuery
import com.demo.authentication.userCredentials.request.UserCredentialsRequest
import norm.query
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class AuthenticationRepository(@Inject private val dataSource: DataSource) {
    fun checkCredentials(userCredentialsRequest: UserCredentialsRequest) =
        CheckCredentialsQuery()
            .query(
                dataSource.connection, CheckCredentialsParams(
                    userCredentialsRequest.username,
                    userCredentialsRequest.password
                )
            )
            .isNotEmpty()
}
