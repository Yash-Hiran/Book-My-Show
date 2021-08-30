package com.demo.book.show.service

import com.demo.book.movie.entity.Movie
import com.demo.book.show.exception.InvalidShowDetailsException
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

        if (validateShowStartTime(showRequest.startTime))
            throw InvalidShowDetailsException("Can not schedule a show for past show time")
        if (!validateShowDate(showRequest))
            throw InvalidShowDetailsException("Show date and start time date does not match")

        val showEndTime = getEndTime(showRequest, movie)

        if (checkOverlapOfShows(showRequest.startTime, showEndTime))
            throw InvalidShowDetailsException("Already have a show scheduled during that time")
        return showRepository.save(showRequest, showEndTime)
    }

    fun getEndTime(
        showRequest: CreateShowRequest,
        movie: Movie
    ): LocalDateTime {
        return showRequest.startTime.plusMinutes(movie.duration.toLong())
    }

    fun checkOverlapOfShows(
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Boolean {
        val showList = allShows()
        return showList.any {
            startTime in it.startTime..it.endTime ||
                    endTime in it.startTime..it.endTime
        }
    }

    fun allShows(): List<Show> {
        return showRepository.findAll()
    }

    fun allShowsByOrder() = showRepository.findAllByOrder()

    fun getAvailableSeatsOfAShow(showId: Int): List<Int> {

        val showCapacityResult = showRepository.getAvailableSeatsOfAShow(showId)
        if (showCapacityResult.isEmpty())
            throw InvalidShowDetailsException("Show Id does not exist")
        val showCapacity = showCapacityResult.first().capacity
        val bookedSeatsList = getBookedSeats(showId)
        val allSeatsList = 1..showCapacity
        val availableSeatsList = allSeatsList.filter { it !in bookedSeatsList }

        if (availableSeatsList.isEmpty())
            throw InvalidShowDetailsException("Show Capacity Full")
        return availableSeatsList
    }

    private fun validateShowDate(showRequest: CreateShowRequest) =
        showRequest.showDate == showRequest.startTime.toLocalDate()

    private fun validateShowStartTime(showStartTime: LocalDateTime) =
        showStartTime < LocalDateTime.now()

    private fun getBookedSeats(showId: Int): List<Int> {
        val bookedSeats = showRepository.getBookedSeatsOfAShow(showId)
        val bookedSeatsList = mutableListOf<Int>()
        for (seatNumber in bookedSeats) {
            bookedSeatsList.add(seatNumber.seatno)
        }
        return bookedSeatsList.toList()
    }
}
