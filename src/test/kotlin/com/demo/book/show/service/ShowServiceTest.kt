package com.demo.book.show.service

import com.demo.book.movie.entity.Movie
import com.demo.book.movie.repository.MovieRepository
import com.demo.book.movie.repository.ShowRepository
import com.demo.book.movie.request.CreateMovieRequest
import com.demo.book.movie.request.CreateShowRequest
import com.demo.book.movie.service.MovieService
import com.demo.book.movie.service.ShowService
import com.demo.book.show.entity.Show
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.neverNullMatcher
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotHaveSameHashCodeAs
import io.kotest.matchers.stats.haveVariance
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.sql.Date
import java.sql.Timestamp

class ShowServiceTest : StringSpec({
    val showRepositoryMock = mockk<ShowRepository>()
    val movieMock = mockk<MovieRepository>()
    val showService = ShowService(showRepositoryMock, movieMock)

    "should get all saved shows" {
        every { showRepositoryMock.findAll() }.returns(
            listOf(
                Show(
                    1, 4, Date.valueOf("2021-10-10"),
                    Timestamp.valueOf("2021-10-10 15:50:00"), Timestamp.valueOf("2021-10-10 15:90:00")
                )
            )
        )

        showService.allShows() shouldBe listOf(
            Show(
                1, 4, Date.valueOf("2021-10-10"),
                Timestamp.valueOf("2021-10-10 15:50:00"), Timestamp.valueOf("2021-10-10 15:90:00")
            )
        )
    }

    "should get empty List when no show exists"{
        every { showRepositoryMock.findAll() }.returns(listOf())
        showService.allShows() shouldBe listOf()
    }
    "should return correct end time"{
        val showRequest = CreateShowRequest(1, Date.valueOf("2021-10-10"), Timestamp.valueOf("2021-10-10 15:00:00"))
        val movie = Movie(1, "Bird Box", 30)
        showService.getEndTime(showRequest, movie) shouldBe Timestamp.valueOf("2021-10-10 15:30:00.0")
    }

    /*"should return false for no overlap"{
        val showRequest = CreateShowRequest(1, Date.valueOf("2021-10-10"), Timestamp.valueOf("2021-10-10 15:00:00"))
        val movie = Movie(1, "Bird Box", 30)
        val endTime = showService.getEndTime(showRequest, movie)
        val isOverlap = showService.checkOverlap(showRequest,endTime)
        isOverlap shouldBe
    }*/
//    "should save a show"{
//        val show= CreateShowRequest(1,Date.valueOf("2021-10-10"),Timestamp.valueOf("2021-10-10 15:50:00"))
//        every { showRepositoryMock.save() return }
//
//    }
})
