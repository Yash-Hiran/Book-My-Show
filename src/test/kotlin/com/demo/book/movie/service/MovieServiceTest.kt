package com.demo.book.movie.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.exception.InvalidMovieDurationException
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.movie.request.CreateMovieRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class MovieServiceTest : StringSpec({

    val movieRepositoryMock = mockk<MovieRepository>()
    val movieService = MovieService(movieRepositoryMock)

    "should save movie to repository" {
        every { movieRepositoryMock.save(any()) }.returns(Movie(1, "Avengers", 90))

        val movieRequest = CreateMovieRequest("Avengers", 90)
        val response = movieService.save(movieRequest)

        response shouldBe Movie(1, "Avengers", 90)
        verify(exactly = 1) { movieRepositoryMock.save(movieRequest) }
    }

    "should throw an exception when duration is less than 5" {
        val movieRequest = CreateMovieRequest("Avengers", 3)
        val exception = shouldThrow<InvalidMovieDurationException> { movieService.save(movieRequest) }

        exception.message shouldBe "Movie duration is less than 5"
    }
})
