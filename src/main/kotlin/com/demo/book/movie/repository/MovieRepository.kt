package com.demo.book.movie.repository

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.MovieRequest
import movie.GetAllMoviesParams
import movie.GetAllMoviesQuery
import movie.SaveMovieParams
import movie.SaveMovieQuery
import norm.query
import java.sql.Timestamp
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class MovieRepository(@Inject private val datasource: DataSource) {

    fun save(movieToSave: MovieRequest): Movie = datasource.connection.use { connection ->
        SaveMovieQuery().query(
            connection,
            SaveMovieParams(
                movieToSave.title,
                Timestamp.from(Instant.ofEpochMilli(movieToSave.startTime)),
                Timestamp.from(Instant.ofEpochMilli(movieToSave.endTime))
            )
        )
    }.map {
        Movie(
            it.id,
            it.title,
            it.startTime.toLocalDateTime(),
            it.endTime.toLocalDateTime()
        )
    }.first()

    fun findAll(): List<Movie> = datasource.connection.use { connection ->
        GetAllMoviesQuery().query(
            connection,
            GetAllMoviesParams()
        )
    }.map {
        Movie(
            it.id,
            it.title,
            it.startTime.toLocalDateTime(),
            it.endTime.toLocalDateTime()
        )
    }
}
