package com.demo.book.movie.repository

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateShowRequest
import com.demo.book.show.entity.Show
import liquibase.pro.packaged.it
import norm.query
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
/*
    fun save(showToSave: CreateShowRequest): Movie = datasource.connection.use { connection ->
       SaveShowQuery().query(
            connection,
           SaveShowParams(
                showToSave.movieId,
               showToSave.showDate,
                       showToSave.
            )
        )
    }.map {
        Movie(
            it.id,
            it.title,
            it.duration
        )
    }.first()*/

    fun findAll(): List<Show> = datasource.connection.use { connection ->
        GetAllShowsQuery().query(
            connection,
            GetAllShowsParams()
        )
    }.map {
        Show(
            it.id,
            it.title,
            it.showDate!!.toLocalDate() ,
            it.startTime.toLocalDateTime(),
            it.duration

        )
    }
}
