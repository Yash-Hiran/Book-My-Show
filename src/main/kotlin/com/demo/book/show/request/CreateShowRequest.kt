package com.demo.book.movie.request

import java.sql.Timestamp
import java.sql.Date

data class CreateShowRequest(val movieId: Int, val showDate: Date, val startTime: Timestamp)
