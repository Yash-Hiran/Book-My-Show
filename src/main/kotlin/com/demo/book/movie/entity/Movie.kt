package com.demo.book.movie.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

data class Movie(
    val id: Int,
    val title: String,
    val duration: Int
)
