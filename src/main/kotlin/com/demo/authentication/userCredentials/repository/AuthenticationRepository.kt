package com.demo.authentication.userCredentials.repository

import authentication.CheckUserQuery
import com.demo.authentication.userCredentials.entity.UserCredentials
import com.demo.book.movie.request.CreateMovieRequest
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class AuthenticationRepository(@Inject private val dataSource: DataSource) {

    fun checkCredentials(userCredentialsRequest: CreateMovieRequest){
        fun checkUserCredential(): UserCredentials = datasource.connection.use { connection ->
            CheckUserQuery.query(
                connection,
                checkUser()
            )
        }.map {
            Movie(
                it.id,
                it.title,
                it.duration
            )
        }
    }
}
