package com.demo.book.movie.request

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CreateMovieRequestTest : StringSpec({

    "should return false if duration is within time limit" {
        val movieRequest = CreateMovieRequest("Batman", 6)
        movieRequest.isNotWithinTimeLimits() shouldBe false
    }

    "should return true if duration is less than time limit" {
        val movieRequest = CreateMovieRequest("Batman", 3)
        movieRequest.isNotWithinTimeLimits() shouldBe true
    }

    "should return true if duration is greater than time limit" {
        val movieRequest = CreateMovieRequest("Batman", 700)
        movieRequest.isNotWithinTimeLimits() shouldBe true
    }
})
