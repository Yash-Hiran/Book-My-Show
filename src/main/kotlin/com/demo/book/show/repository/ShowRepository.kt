package com.demo.book.movie.repository

import com.demo.book.movie.entity.Movie
import com.demo.book.show.entity.Show
import liquibase.pro.packaged.it
import norm.query
import show.GetAllShowsParams
import show.GetAllShowsQuery
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class ShowRepository(@Inject private val datasource: DataSource) {

    /*fun save(movieToSave: CreateMovieRequest): Movie = datasource.connection.use { connection ->
       SaveMovieQuery().query(
            connection,
            SaveMovieParams(
                movieToSave.title,
                movieToSave.duration
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
