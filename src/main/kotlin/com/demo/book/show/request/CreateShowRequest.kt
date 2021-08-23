package com.demo.book.movie.request

import com.demo.book.movie.entity.Movie
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateShowRequest(val movieId : Int, val date : LocalDate, val startTime : LocalDateTime, val endTime : LocalDateTime)
