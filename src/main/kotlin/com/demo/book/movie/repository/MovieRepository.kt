package com.demo.book.movie.repository

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.request.CreateMovieRequest
import movie.*
import norm.query
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class MovieRepository(@Inject private val datasource: DataSource) {

    fun save(movieToSave: CreateMovieRequest): Movie = datasource.connection.use { connection ->
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
            it.duration
        )
    }
    fun getMovieWithId(id:Int) = datasource.connection.use { connection ->
        GetMovieByIdQuery().query(
            connection,
            GetMovieByIdParams(id)
        ).map {
            Movie(
                it.id,
                it.title,
                it.duration
            )
        }

}
