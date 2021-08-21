package com.demo.authentication.api

import authentication.CheckUserQuery
import com.demo.authentication.userCredentials.service.AuthenticationService
import com.demo.book.movie.entity.Movie
import com.nimbusds.jose.shaded.json.JSONObject
import io.micronaut.context.annotation.Parameter
import io.micronaut.http.BasicAuth
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.UsernamePasswordCredentials
import norm.query
import javax.inject.Inject

@Controller
class AuthenticationApi(@Inject private val authenticationService: AuthenticationService){
    @Post("/login")
    fun login(@Header username: String, password: String): HttpResponse<Boolean> {
        val isCorrect = authenticationService.checkCredentials(BasicAuth(username, password))
        return HttpResponse.ok(isCorrect)
    }

    @Post("/logout")
    fun logout() : HttpResponse<Boolean>{
        return HttpResponse.ok(true)
    }
}
