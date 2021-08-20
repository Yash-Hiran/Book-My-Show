package com.demo.authentication.api

import com.demo.book.movie.entity.Movie
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import javax.inject.Inject

@Controller
class AuthenticationApi(){

    @Get("/login")
    fun login(): HttpResponse<Boolean> {
        return HttpResponse.ok(true)
    }

    @Post("/logout")
    fun logout() : HttpResponse<Boolean>{
        return HttpResponse.ok(true)
    }
}
