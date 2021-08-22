package com.demo.book.movie.request

data class CreateMovieRequest(val title: String, val duration: Int) {
    fun isNotWithinTimeLimit(): Boolean {
        return duration < 5
    }
}
