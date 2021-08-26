package com.demo.book.show.request

import java.time.LocalDate
import java.time.LocalDateTime

data class CreateShowRequest(val movieId: Int, val showDate: LocalDate, val startTime: LocalDateTime)
