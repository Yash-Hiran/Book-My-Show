package com.demo.book.movie.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.exception.InvalidMovieDetailsException
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

    "should save movie to repository when duration is within time limit" {
        every { movieRepositoryMock.save(any()) }.returns(Movie(1, "Avengers", 90))

        val movieRequest = CreateMovieRequest("Avengers", 90)
        val response = movieService.save(movieRequest)

        response shouldBe Movie(1, "Avengers", 90)
        verify(exactly = 1) { movieRepositoryMock.save(movieRequest) }
    }

    "should throw an exception when duration is less than 5 minutes" {
        val movieRequest = CreateMovieRequest("Avengers", 3)
        val exception = shouldThrow<InvalidMovieDetailsException> { movieService.save(movieRequest) }

        exception.message shouldBe "Movie Duration must be between 5 minutes and" +
                " 360 minutes(both included)"
    }

    "should throw an exception when duration is more than 360 minutes" {
        val movieRequest = CreateMovieRequest("Avengers", 370)
        val exception = shouldThrow<InvalidMovieDetailsException> { movieService.save(movieRequest) }

        exception.message shouldBe "Movie Duration must be between 5 minutes and" +
                " 360 minutes(both included)"
    }

    "should save movie to repository when duration is 5 minutes" {
        every { movieRepositoryMock.save(any()) }.returns(Movie(1, "Avengers", 5))

        val movieRequest = CreateMovieRequest("Avengers", 5)
        val response = movieService.save(movieRequest)

        response shouldBe Movie(1, "Avengers", 5)
        verify(exactly = 1) { movieRepositoryMock.save(movieRequest) }
    }

    "should save movie to repository when duration is 360 minutes" {
        every { movieRepositoryMock.save(any()) }.returns(Movie(1, "Avengers", 360))

        val movieRequest = CreateMovieRequest("Avengers", 360)
        val response = movieService.save(movieRequest)

        response shouldBe Movie(1, "Avengers", 360)
        verify(exactly = 1) { movieRepositoryMock.save(movieRequest) }
    }

    "should get Movie object when searching by id that exists" {
        every { movieRepositoryMock.getMovieWithId(4) }.returns(listOf(Movie(4, "Avengers", 90)))
        val movie = movieService.getMovieWithId(4)
        movie shouldBe Movie(4, "Avengers", 90)
    }

    "should return an exception when searching a non existent id" {
        every { movieRepositoryMock.getMovieWithId(any()) }.returns(listOf())
        val movie = shouldThrow<InvalidMovieDetailsException> { movieService.getMovieWithId(4) }
        movie.message shouldBe "Movie Id does not exist"
    }
})
