package com.demo.book.show.entity
import com.demo.book.movie.entity.Movie
import java.time.LocalDate
import java.time.LocalDateTime


data class Show(
    val id: Int,
    val movie: Movie,
    val date: LocalDate,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)
