package com.demo.book.movie.request

data class CreateMovieRequest(val title: String, val duration: Int){
    fun isNotWithinTimeLimits(): Boolean{
        return (duration<5 || duration>360)
    }
}
