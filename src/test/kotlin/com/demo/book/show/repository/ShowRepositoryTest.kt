package com.demo.book.show.repository

import com.demo.book.BookingIntegrationSpec
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.show.entity.Show
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import norm.executeCommand
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.sql.DataSource

@MicronautTest
class ShowRepositoryTest(@Inject override var dataSource: DataSource) : BookingIntegrationSpec() {
    override fun clearData() = dataSource.connection.use {
        it.executeCommand("TRUNCATE TABLE shows RESTART IDENTITY CASCADE;")
        it.executeCommand("TRUNCATE TABLE movies RESTART IDENTITY CASCADE;")
    }

    private val showRepository = ShowRepository(dataSource)
    private val movieRepository = MovieRepository(dataSource)

    init {

        "should save and return the saved show" {
            // given
            val startTime = "2021-09-25T15:50:00"
            val movie = newMovieRequest(10)
            movieRepository.save(movie)

            // when
            val show = newShowRequest("2021-09-25", startTime)
            val result = showRepository.save(show, LocalDateTime.of(2021, 9, 25, 16, 0, 0))

            // then
            result shouldBe Show(
                1,
                1,
                LocalDate.of(2021, 9, 25),
                LocalDateTime.of(2021, 9, 25, 15, 50, 0),
                LocalDateTime.of(2021, 9, 25, 16, 0, 0)
            )
        }

        "should return all the shows" {
            // given
            val startTime = "2021-09-25T15:50:00"
            val startTime2 = "2021-09-26T15:50:00"
            val movie = newMovieRequest(10)
            movieRepository.save(movie)

            // when
            val show = newShowRequest("2021-09-25", startTime)
            showRepository.save(show, LocalDateTime.of(2021, 9, 25, 16, 0, 0))
            val show2 = newShowRequest("2021-09-26", startTime2)
            showRepository.save(show2, LocalDateTime.of(2021, 9, 26, 16, 0, 0))

            // then
            val result = showRepository.findAll()
            result shouldBe listOf(
                Show(
                    2,
                    1,
                    LocalDate.of(2021, 9, 26),
                    LocalDateTime.of(2021, 9, 26, 15, 50, 0),
                    LocalDateTime.of(2021, 9, 26, 16, 0, 0)
                ),
                Show(
                    1,
                    1,
                    LocalDate.of(2021, 9, 25),
                    LocalDateTime.of(2021, 9, 25, 15, 50, 0),
                    LocalDateTime.of(2021, 9, 25, 16, 0, 0)
                )
            )
        }
    }
}
