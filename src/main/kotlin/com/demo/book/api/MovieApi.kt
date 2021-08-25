package com.demo.book.api

import com.demo.authentication.exception.InvalidUsernameOrPasswordException
import com.demo.authentication.userCredentials.repository.AuthenticationRepository
import com.demo.authentication.userCredentials.service.AuthenticationService
import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.movie.service.MovieService
import io.micronaut.http.BasicAuth
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import javax.inject.Inject
import javax.sql.DataSource

@Controller
class MovieApi(@Inject val movieService: MovieService, @Inject private val dataSource: DataSource) {

    @Get("/movies")
    fun allMovies(): HttpResponse<List<Movie>> {
        return HttpResponse.ok(movieService.allMovies())
    }

    @Post("/movies")
    fun saveMovie(
        @Header basicAuth: BasicAuth,
        @Body movieRequest: CreateMovieRequest
    ): HttpResponse<Int> {
        if (AuthenticationService(AuthenticationRepository(dataSource))
                .checkCredentials(basicAuth)
        ) return HttpResponse.ok(movieService.save(movieRequest).id)
        else throw InvalidUsernameOrPasswordException("com.movie.api.service")
    }
}
