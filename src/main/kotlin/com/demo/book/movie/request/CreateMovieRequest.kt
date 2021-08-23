package com.demo.book.movie.request

data class CreateMovieRequest(val title: String, val duration: Int) {
    // TODO: refactor the condition using range and using expression function
    fun isNotWithinTimeLimits(): Boolean {
        return (duration < 5 || duration > 360)
    }
}
