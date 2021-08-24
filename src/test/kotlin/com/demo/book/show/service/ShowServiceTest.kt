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
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime

class ShowServiceTest : StringSpec({
    val showRepositoryMock = mockk<ShowRepository>()
    val movieMock = mockk<MovieRepository>()
    val showService = ShowService(showRepositoryMock, movieMock)

//    "should save a show"{
//        val showRequest =
//            CreateShowRequest(1, LocalDate.parse("2021-10-10"), LocalDateTime.parse("2021-10-10T12:30:00"))
//        val movie = Movie(1, "Bird Box", 30)
//        val endTime = showService.getEndTime(showRequest, movie)
//        every { showRepositoryMock.save(showRequest, endTime) }.returns(
//            Show(
//                1,
//                1,
//                LocalDate.parse("2021-10-10"),
//                LocalDateTime.parse("2021-10-10T12:30:00"),
//                LocalDateTime.parse("2021-10-10T13:00:00")
//            )
//        )
//        verify { showRepositoryMock.save(showRequest,endTime) }
//    }

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

    "should return false for no overlap"{
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
        val isOverlap = showService.checkOverlap(showRequest, endTime)
        isOverlap shouldBe false
    }

    "should return true for overlap"{
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
            CreateShowRequest(1, LocalDate.parse("2021-10-10"), LocalDateTime.parse("2021-10-10T12:30:00"))
        val movie = Movie(1, "Bird Box", 30)
        val endTime = showService.getEndTime(showRequest, movie)
        val isOverlap = showService.checkOverlap(showRequest, endTime)
        isOverlap shouldBe true
    }



})

