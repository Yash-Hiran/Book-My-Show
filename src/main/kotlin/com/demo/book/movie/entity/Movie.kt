package com.demo.book.movie.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Movie(
    val id: Int,
    val title: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    val startTime: LocalDateTime?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    val endTime: LocalDateTime?
)
