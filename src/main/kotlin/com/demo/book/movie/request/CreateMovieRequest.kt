package com.demo.book.movie.request

data class CreateMovieRequest(val title: String, val duration: Int) {
    private val MINIMUM_DURATION_IN_MINUTES = 5
    private val MAXIMUM_DURATION_IN_MINUTES = 360

    fun isNotWithinTimeLimits() =
        duration !in MINIMUM_DURATION_IN_MINUTES..MAXIMUM_DURATION_IN_MINUTES
}
