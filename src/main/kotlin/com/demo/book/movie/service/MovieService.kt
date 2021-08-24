package com.demo.book.movie.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.exception.InvalidMovieDurationException
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.movie.request.CreateMovieRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieService(@Inject val movieRepository: MovieRepository) {
    fun save(movieRequest: CreateMovieRequest): Movie {
        if (movieRequest.isNotWithinTimeLimits())
            throw InvalidMovieDurationException(
                message = "Movie Duration is not within time limit. It must be greater than 5 and less than 360"
            )
        return movieRepository.save(movieRequest)
    }

    fun allMovies(): List<Movie> {
        return movieRepository.findAll()
    }
}
