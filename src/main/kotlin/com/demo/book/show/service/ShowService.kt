package com.demo.book.movie.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.exception.InvalidMovieDetailsException
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
        val endTimeInTimeStamp = getEndTime(showRequest, movie)
        if(checkOverlap(showRequest, endTimeInTimeStamp))
            throw InvalidMovieDetailsException("Overlap in show timings found")
        return showRepository.save(showRequest, endTimeInTimeStamp)
    }

    fun getEndTime(
        showRequest: CreateShowRequest,
        movie: Movie
    ): Timestamp {
        val endTime = showRequest.startTime.toLocalDateTime().plusMinutes(movie.duration.toLong())
        return Timestamp.valueOf(endTime)
    }

    fun checkOverlap(
        showRequest: CreateShowRequest,
        endTimeInTimeStamp: Timestamp
    ) : Boolean{
        val showList = allShows()
        return showList.any{ showRequest.startTime in it.startTime..it.endTime || endTimeInTimeStamp in it.startTime..it.endTime }
    }

    fun allShows(): List<Show> {
        return showRepository.findAll()
    }
}
