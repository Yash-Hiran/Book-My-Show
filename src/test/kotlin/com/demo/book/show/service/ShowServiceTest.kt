package com.demo.book.show.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.exception.InvalidMovieDetailsException
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.movie.service.MovieService
import com.demo.book.show.repository.ShowRepository
import com.demo.book.show.request.CreateShowRequest
import com.demo.book.show.entity.Show
import com.demo.book.show.exception.InvalidShowDetailsException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime

class ShowServiceTest : StringSpec({
    val showRepositoryMock = mockk<ShowRepository>()
    val movieRepositoryMock = mockk<MovieRepository>()
    val showService = ShowService(showRepositoryMock, movieRepositoryMock)
    val movieService = MovieService(movieRepositoryMock)

    "should get all saved shows" {
        every { showRepositoryMock.findAll() }.returns(
            listOf(
                Show(
                    1, 4, LocalDate.parse("2021-10-10"),
                    LocalDateTime.parse("2021-10-10T15:00:00"), LocalDateTime.parse("2021-10-10T15:40:00")
                )
            )
        )

        showService.allShows() shouldBe listOf(
            Show(
                1, 4, LocalDate.parse("2021-10-10"),
                LocalDateTime.parse("2021-10-10T15:00:00"), LocalDateTime.parse("2021-10-10T15:40:00")
            )
        )
    }

    "should get empty List when no show exists" {
        every { showRepositoryMock.findAll() }.returns(listOf())
        showService.allShows() shouldBe listOf()
    }

    "should save a show" {
        val showRequest =
            CreateShowRequest(
                1,
                LocalDate.parse("2021-10-10"),
                LocalDateTime.parse("2021-10-10T12:30:00")
            )

        val movie = Movie(1, "Bird Box", 30)
        val endTime = showService.getEndTime(showRequest, movie)
        every { movieRepositoryMock.getMovieWithId(any()) }.returns(listOf(Movie(1, "AAJA SANAM", 30)))
        every { showRepositoryMock.save(showRequest, endTime) }.returns(
            Show(
                1,
                1,
                LocalDate.parse("2021-10-10"),
                LocalDateTime.parse("2021-10-10T12:30:00"),
                LocalDateTime.parse("2021-10-10T13:00:00")
            )
        )
        every { showRepositoryMock.findAll() }.returns(listOf())

        showService.save(showRequest)
        verify(exactly = 1) { showRepositoryMock.save(showRequest, endTime) }
    }

    "should throw an exception if show is overlapping" {

        val createShowRequest =
            CreateShowRequest(1, LocalDate.parse("2021-10-10"), LocalDateTime.parse("2021-10-10T12:30:00"))
        every { movieRepositoryMock.getMovieWithId(any()) }.returns(listOf(Movie(1, "harry potter", 30)))
        val movie = movieService.getMovieWithId(createShowRequest.movieId)
        val endTime = showService.getEndTime(createShowRequest, movie)

        every { showRepositoryMock.save(createShowRequest, endTime) }.returns(
            Show(
                1,
                1,
                LocalDate.parse("2021-10-10"),
                LocalDateTime.parse("2021-10-10T12:30:00"),
                LocalDateTime.parse("2021-10-10T13:00:00")
            )
        )

        every { showRepositoryMock.findAll() }.returns(
            listOf(
                Show(
                    1,
                    1,
                    LocalDate.parse("2021-10-10"),
                    LocalDateTime.parse("2021-10-10T12:30:00"),
                    LocalDateTime.parse("2021-10-10T13:00:00")
                )
            )
        )
        val createSecondShowRequest =
            CreateShowRequest(1, LocalDate.parse("2021-10-10"), LocalDateTime.parse("2021-10-10T12:30:00"))
        val exception = shouldThrow<InvalidShowDetailsException> { showService.save(createSecondShowRequest) }
        exception.message shouldBe "Already have a show scheduled during that time"
    }

    "should throw an exception if movie does not exist and we try to save a show for that movie" {
        every { movieRepositoryMock.getMovieWithId(any()) }.returns(listOf())
        val createShowRequest =
            CreateShowRequest(1, LocalDate.parse("2021-10-10"), LocalDateTime.parse("2021-10-10T12:30:00"))
        val exception = shouldThrow<InvalidMovieDetailsException> { showService.save(createShowRequest) }
        exception.message shouldBe "Movie Id does not exist"
    }

    "should throw an exception if show time is invalid" {
        every { movieRepositoryMock.getMovieWithId(any()) }.returns(listOf(Movie(1, "harry potter", 30)))
        val createShowRequest =
            CreateShowRequest(1, LocalDate.parse("2020-10-10"), LocalDateTime.parse("2020-10-10T12:30:00"))
        val exception = shouldThrow<InvalidShowDetailsException> { showService.save(createShowRequest) }
        exception.message shouldBe "Can not schedule a show for past show time"
    }

    "should throw an exception if show start date and start time does not match" {
        every { movieRepositoryMock.getMovieWithId(any()) }.returns(listOf(Movie(1, "harry potter", 30)))
        val createShowRequest =
            CreateShowRequest(1, LocalDate.parse("2021-10-10"), LocalDateTime.parse("2021-10-12T12:30:00"))
        val exception = shouldThrow<InvalidShowDetailsException> { showService.save(createShowRequest) }
        exception.message shouldBe "Show date and start time date does not match"
    }
})
