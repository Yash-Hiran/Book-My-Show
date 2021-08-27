package com.demo.book.api

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.movie.service.MovieService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import javax.inject.Inject

@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
class MovieApi(@Inject private val movieService: MovieService) {
    @Get("/movies")
    fun allMovies(): HttpResponse<List<Movie>> {
        return HttpResponse.ok(movieService.allMovies())
    }

    @Post("/movies")
    fun saveMovie(@Body movieRequest: CreateMovieRequest): HttpResponse<Int> {
        return HttpResponse.ok(movieService.save(movieRequest).id)
    }
}
