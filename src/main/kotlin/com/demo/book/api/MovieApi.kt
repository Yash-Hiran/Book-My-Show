package com.demo.book.api

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.service.MovieService
import com.demo.book.movie.request.MovieRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import javax.inject.Inject

@Controller
class MovieApi(@Inject val movieService: MovieService) {

    @Get("/movies")
    fun allMovies(): HttpResponse<List<Movie>> {
        return HttpResponse.ok(movieService.allMovies())
    }

    @Post("/movies")
    fun saveMovie(@Body movieRequest: MovieRequest): MutableHttpResponse<Int> {
        // if movie duration is valid, add movie. Otherwise, return bad request
        if (movieRequest.duration in 5..360)
            return HttpResponse.ok(movieService.save(movieRequest).id)
        return HttpResponse.badRequest()
    }
}
