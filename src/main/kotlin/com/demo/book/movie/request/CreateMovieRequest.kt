package com.demo.book.movie.request

data class CreateMovieRequest(val title: String, val duration: Int) {
    private val MIN_TIME_LIMIT = 5
    private val MAX_TIME_LIMIT = 360

    fun isNotWithinTimeLimits() = duration !in MIN_TIME_LIMIT..MAX_TIME_LIMIT
}
