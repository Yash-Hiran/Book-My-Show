package com.demo.book.movie.service

import com.demo.book.movie.repository.MovieRepository
import com.demo.book.movie.repository.ShowRepository
import com.demo.book.movie.request.CreateShowRequest
import com.demo.book.show.entity.Show
import java.sql.Timestamp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowService(@Inject val showRepository: ShowRepository, val movieRepository: MovieRepository) {
    fun save(showRequest: CreateShowRequest): Show {

        val movieService = MovieService(movieRepository)
        val movie = movieService.getMovieWithId(showRequest.movieId)
        val endTime = showRequest.startTime.toLocalDateTime().plusMinutes(movie.duration.toLong())
        val endTimeInTimeStamp = Timestamp.valueOf(endTime)
        return showRepository.save(showRequest,endTimeInTimeStamp)
    }

    fun allShows(): List<Show> {
        return showRepository.findAll()
    }
}

