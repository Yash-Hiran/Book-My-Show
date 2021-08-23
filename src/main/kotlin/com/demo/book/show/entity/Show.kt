package com.demo.book.show.entity
import com.demo.book.movie.entity.Movie
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


data class Show(
    val id: Int,
    val movieId: Int,
    val showDate : Date,
    val startTime: Timestamp,
    val endTime : Timestamp
)
