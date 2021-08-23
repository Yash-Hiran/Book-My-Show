package com.demo.book.show.entity
import com.demo.book.movie.entity.Movie
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Show(
    val id: Int,
    val movieTitle: String,
    val showDate : LocalDate,
    val startTime: LocalDateTime,
    val duration: Int
)
