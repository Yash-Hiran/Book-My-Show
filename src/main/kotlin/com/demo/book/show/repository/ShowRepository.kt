package com.demo.book.movie.repository

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateShowRequest
import com.demo.book.show.entity.Show
import norm.query
import java.sql.Date
import show.GetAllShowsParams
import show.GetAllShowsQuery
import show.SaveShowParams
import show.SaveShowQuery
import java.sql.Timestamp
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class ShowRepository(@Inject private val datasource: DataSource) {
    fun save(showToSave: CreateShowRequest): Show = datasource.connection.use { connection ->
       SaveShowQuery().query(
            connection,
           SaveShowParams(
                showToSave.movieId,
               showToSave.showDate,
               showToSave.startTime,
               showToSave.endTime
            )
        )
    }.map {
        Show(
            it.id,
            it.movieId,
            it.showDate,
            it.startTime,
            it.endTime

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
            it.showDate,
            it.startTime,
            it.endTime

        )
    }
}
