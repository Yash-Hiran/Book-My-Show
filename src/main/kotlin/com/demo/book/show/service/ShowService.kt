package com.demo.book.show.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.exception.InvalidMovieDetailsException
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.show.repository.ShowRepository
import com.demo.book.movie.service.MovieService
import com.demo.book.show.request.CreateShowRequest
import com.demo.book.show.entity.Show
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowService(@Inject val showRepository: ShowRepository, private val movieRepository: MovieRepository) {

    fun save(showRequest: CreateShowRequest): Show {

        val movieService = MovieService(movieRepository)
        val movie = movieService.getMovieWithId(showRequest.movieId)
        val endTimeInTimeStamp = getEndTime(showRequest, movie)
        if (checkOverlap(showRequest, endTimeInTimeStamp))
            throw InvalidMovieDetailsException("Overlap in show timings found")
        return showRepository.save(showRequest, endTimeInTimeStamp)
    }

    fun getEndTime(
        showRequest: CreateShowRequest,
        movie: Movie
    ): LocalDateTime {
        return showRequest.startTime.plusMinutes(movie.duration.toLong())
    }

    fun checkOverlap(
        showRequest: CreateShowRequest,
        endTimeInTimeStamp: LocalDateTime
    ): Boolean {
        val showList = allShows()
        return showList.any {
            showRequest.startTime in it.startTime..it.endTime ||
                    endTimeInTimeStamp in it.startTime..it.endTime
        }
    }

    fun allShows(): List<Show> {
        return showRepository.findAll()
    }

    fun allPastShows(): List<Show> {
        return showRepository.findAllPastShows()
    }
}
