package com.demo.book.movie.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.repository.ShowRepository
import com.demo.book.show.entity.Show
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowService(@Inject val showRepository: ShowRepository) {
   /* fun save(showRequest: CreateShowRequest): Movie {
        return showRepository.save(showRequest)
    }*/

    fun allShows(): List<Show> {
        return showRepository.findAll()
    }
}

