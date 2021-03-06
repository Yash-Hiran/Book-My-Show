package com.demo.book.show.repository

import com.demo.book.show.request.CreateShowRequest
import com.demo.book.show.entity.Show
import norm.command
import norm.query
import show.*
import ticket.*
import ticket.GetBookedSeatsParams
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class ShowRepository(@Inject private val datasource: DataSource) {

    fun updatePrice(showId: Int, price: Int) = datasource.connection.use { connection ->
        UpdatePriceOfShowsCommand().command(
            connection,
            UpdatePriceOfShowsParams(
                price,
                showId
            )
        )
    }

    fun getShowById(showId: Int): List<Show> = datasource.connection.use { connection ->
        GetShowByShowidQuery().query(
            connection,
            GetShowByShowidParams(showId)
        )
    }.map {
        Show(
            it.id,
            it.movieId,
            it.showDate.toLocalDate(),
            it.startTime.toLocalDateTime(),
            it.endTime.toLocalDateTime(),
            it.capacity,
            it.price
        )
    }

    fun save(showToSave: CreateShowRequest, endTime: LocalDateTime): Show = datasource.connection.use { connection ->
        SaveShowQuery().query(
            connection,
            SaveShowParams(
                showToSave.movieId,
                Date.valueOf(showToSave.showDate),
                Timestamp.valueOf(showToSave.startTime),
                Timestamp.valueOf(endTime),
                showToSave.capacity
            )
        )
    }.map {
        Show(
            it.id,
            it.movieId,
            it.showDate.toLocalDate(),
            it.startTime.toLocalDateTime(),
            it.endTime.toLocalDateTime(),
            it.capacity,
            it.price
        )
    }.first()

    fun findAll(): List<Show> = datasource.connection.use { connection ->
        GetAllShowsQuery().query(
            connection,
            GetAllShowsParams()
        )
    }.map {
        Show(
            it.id,
            it.movieId,
            it.showDate.toLocalDate(),
            it.startTime.toLocalDateTime(),
            it.endTime.toLocalDateTime(),
            it.capacity,
            it.price
        )
    }

    fun findAllByOrder(): Map<String, List<Show>> = mapOf(
        Pair("Past:", findAllPastShows()),
        Pair("Ongoing:", findAllOngoingShows()),
        Pair("Upcoming:", findAllUpcomingShows())
    )

    private fun findAllPastShows(): List<Show> = datasource.connection.use { connection ->
        GetAllShowsQuery().query(
            connection,
            GetAllShowsParams()
        )
    }.sortedByDescending { it.endTime.toLocalDateTime() }
        .filter { it.endTime.toLocalDateTime() < LocalDateTime.now() }
        .map {
            Show(
                it.id,
                it.movieId,
                it.showDate.toLocalDate(),
                it.startTime.toLocalDateTime(),
                it.endTime.toLocalDateTime(),
                it.price
            )
        }

    private fun findAllUpcomingShows(): List<Show> = datasource.connection.use { connection ->
        GetAllShowsQuery().query(
            connection,
            GetAllShowsParams()
        )
    }.sortedBy { it.startTime.toLocalDateTime() }
        .filter { it.startTime.toLocalDateTime() > LocalDateTime.now() }
        .map {
            Show(
                it.id,
                it.movieId,
                it.showDate.toLocalDate(),
                it.startTime.toLocalDateTime(),
                it.endTime.toLocalDateTime(),
                it.capacity,
                it.price
            )
        }

    private fun findAllOngoingShows(): List<Show> = datasource.connection.use { connection ->
        GetAllShowsQuery().query(
            connection,
            GetAllShowsParams()
        )
    }.sortedBy { it.endTime.toLocalDateTime() }
        .filter {
            it.startTime.toLocalDateTime() < LocalDateTime.now() &&
                    it.endTime.toLocalDateTime() > LocalDateTime.now()
        }
        .map {
            Show(
                it.id,
                it.movieId,
                it.showDate.toLocalDate(),
                it.startTime.toLocalDateTime(),
                it.endTime.toLocalDateTime(),
                it.capacity,
                it.price
            )
        }

    fun getAvailableSeatsOfAShow(id: Int) = datasource.connection.use { connection ->
        GetShowCapacityQuery().query(
            connection,
            GetShowCapacityParams(id)
        )
    }

    fun getBookedSeatsOfAShow(showId: Int) = datasource.connection.use { connection ->
        GetBookedSeatsQuery().query(
            connection,
            GetBookedSeatsParams(showId)
        )
    }
}
