package com.demo.book.movie.request

import com.demo.book.movie.entity.Movie
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.sql.Date

data class CreateShowRequest(val movieId: Int, val showDate: Date, val startTime: Timestamp) {
//init{end = start + movie.duration}
}
