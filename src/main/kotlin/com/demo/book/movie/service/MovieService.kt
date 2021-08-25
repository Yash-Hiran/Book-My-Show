package com.demo.book.movie.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.exception.InvalidMovieDetailsException
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.movie.request.CreateMovieRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieService(@Inject val movieRepository: MovieRepository) {
    fun save(movieRequest: CreateMovieRequest): Movie {
        if (movieRequest.isNotWithinTimeLimits())
            throw InvalidMovieDetailsException(
                message = "Movie Duration must be between 5 minutes and" +
                        " 360 minutes(both included)"
            )
        return movieRepository.save(movieRequest)
    }

    fun allMovies(): List<Movie> {
        return movieRepository.findAll()
    }

    fun getMovieWithId(id: Int): Movie {
        val movieList = movieRepository.getMovieWithId(id)
        if (movieList.isEmpty()) {
            throw InvalidMovieDetailsException("Movie Id does not exist")
        }
        return movieList.first()
    }
}
