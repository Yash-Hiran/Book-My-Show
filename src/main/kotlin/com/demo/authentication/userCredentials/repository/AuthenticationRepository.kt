package com.demo.authentication.userCredentials.repository

import authentication.CheckUserParams
import authentication.CheckUserQuery
import com.demo.authentication.userCredentials.entity.UserCredentials
import com.demo.book.movie.request.CreateMovieRequest
import io.micronaut.http.BasicAuth
import norm.query
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class AuthenticationRepository(@Inject private val dataSource: DataSource) {

    fun checkCredentials(credentials: BasicAuth): Boolean {
        val users = CheckUserQuery().query(dataSource.connection, CheckUserParams(credentials.username, credentials.password))
        return users.size == 1
    }
}
