package com.demo.book.movie.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.movie.request.CreateMovieRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieService(@Inject val movieRepository: MovieRepository) {
    fun save(movieRequest: CreateMovieRequest): Movie {
        return movieRepository.save(movieRequest)
    }

    fun allMovies(): List<Movie> {
        return movieRepository.findAll()
    }
}
