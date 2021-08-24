package com.demo.book.movie.request

data class CreateMovieRequest(val title: String, val duration: Int) {
    // TODO: refactor the condition using range and using expression function
    private val MIN_TIME_LIMIT = 5
    private val MAX_TIME_LIMIT = 360
    fun isNotWithinTimeLimits(): Boolean {
        return (duration < MIN_TIME_LIMIT || duration > MAX_TIME_LIMIT)
    }
}
