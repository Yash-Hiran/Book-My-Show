package com.demo.book.show.entity

import java.sql.Date
import java.sql.Timestamp

data class Show(
    val id: Int,
    val movieId: Int,
    val showDate: Date,
    val startTime: Timestamp,
    val endTime: Timestamp
)
