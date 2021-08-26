package com.demo.book.show.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.show.repository.ShowRepository
import com.demo.book.show.request.CreateShowRequest
import com.demo.book.show.entity.Show
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class ShowServiceTest : StringSpec({
    val showRepositoryMock = mockk<ShowRepository>()
    val movieMock = mockk<MovieRepository>()
    val showService = ShowService(showRepositoryMock, movieMock)

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

    "should return correct end time" {
        val showRequest =
            CreateShowRequest(
                1, LocalDate.parse("2021-10-10"),
                LocalDateTime.parse("2021-10-10T15:00:00")
            )
        val movie = Movie(1, "Bird Box", 30)
        showService.getEndTime(showRequest, movie) shouldBe LocalDateTime.parse("2021-10-10T15:30:00.0")
    }

    "should return false for no overlap" {
        every { showRepositoryMock.findAll() }.returns(
            listOf(
                Show(
                    1,
                    1,
                    LocalDate.parse("2021-10-10"),
                    LocalDateTime.parse("2021-10-10T12:00:00"),
                    LocalDateTime.parse("2021-10-10T12:50:00")
                )
            )
        )
        val showRequest =
            CreateShowRequest(1, LocalDate.parse("2021-10-10"), LocalDateTime.parse("2021-10-10T15:00:00"))
        val movie = Movie(1, "Bird Box", 30)
        val endTime = showService.getEndTime(showRequest, movie)
        val isOverlap = showService.checkOverlapOfShows(showRequest.startTime, endTime)
        isOverlap shouldBe false
    }

    "should return true for overlap" {
        every { showRepositoryMock.findAll() }.returns(
            listOf(
                Show(
                    1,
                    1,
                    LocalDate.parse("2021-10-10"),
                    LocalDateTime.parse("2021-10-10T12:00:00"),
                    LocalDateTime.parse("2021-10-10T12:50:00")
                )
            )
        )
        val showRequest =
            CreateShowRequest(
                1,
                LocalDate.parse("2021-10-10"),
                LocalDateTime.parse("2021-10-10T12:30:00")
            )
        val movie = Movie(1, "Bird Box", 30)
        val endTime = showService.getEndTime(showRequest, movie)
        val isOverlap = showService.checkOverlapOfShows(showRequest.startTime, endTime)
        isOverlap shouldBe true
    }

    "should get a map with all shows sorted by categories" {
        every { showRepositoryMock.findAllByOrder() }.returns(
            mapOf(
                Pair(
                    "Past:", listOf(
                        Show(
                            1,
                            1,
                            LocalDate.parse("2020-06-09"),
                            LocalDateTime.parse("2020-06-09T12:00:00"),
                            LocalDateTime.parse("2020-06-09T15:00:00")
                        )
                    )
                ), Pair(
                    "Ongoing :", listOf(
                        Show(
                            2,
                            1,
                            LocalDate.parse("2021-08-26"),
                            LocalDateTime.parse("2021-08-26T11:00:00"),
                            LocalDateTime.parse("2021-08-26T15:00:00")
                        )
                    )
                ),
                Pair(
                    "Upcoming :", listOf(
                        Show(
                            3,
                            1,
                            LocalDate.parse("2021-09-26"),
                            LocalDateTime.parse("2021-09-26T11:00:00"),
                            LocalDateTime.parse("2021-09-26T15:00:00")
                        ),
                        Show(
                            5,
                            1,
                            LocalDate.parse("2021-09-26"),
                            LocalDateTime.parse("2021-09-26T11:00:00"),
                            LocalDateTime.parse("2021-09-26T15:00:00")
                        )
                    )
                )
            )
        )

        showService.allShowsByOrder() shouldBe mapOf(
            Pair(
                "Past:", listOf(
                    Show(
                        1,
                        1,
                        LocalDate.parse("2020-06-09"),
                        LocalDateTime.parse("2020-06-09T12:00:00"),
                        LocalDateTime.parse("2020-06-09T15:00:00")
                    )
                )
            ), Pair(
                "Ongoing :", listOf(
                    Show(
                        2,
                        1,
                        LocalDate.parse("2021-08-26"),
                        LocalDateTime.parse("2021-08-26T11:00:00"),
                        LocalDateTime.parse("2021-08-26T15:00:00")
                    )
                )
            ),
            Pair(
                "Upcoming :", listOf(
                    Show(
                        3,
                        1,
                        LocalDate.parse("2021-09-26"),
                        LocalDateTime.parse("2021-09-26T11:00:00"),
                        LocalDateTime.parse("2021-09-26T15:00:00")
                    ),
                    Show(
                        5,
                        1,
                        LocalDate.parse("2021-09-26"),
                        LocalDateTime.parse("2021-09-26T11:00:00"),
                        LocalDateTime.parse("2021-09-26T15:00:00")
                    )
                )
            )
        )
    }
})
