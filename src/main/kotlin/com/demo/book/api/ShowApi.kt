package com.demo.book.api

import com.demo.book.show.entity.Show
import com.demo.book.show.request.CreateShowRequest
import com.demo.book.show.service.ShowService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import javax.inject.Inject

@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
class ShowApi(@Inject val showService: ShowService) {

    @Get("/shows")
    fun allShowsByOrder(): HttpResponse<Map<String, List<Show>>> {
        return HttpResponse.ok(showService.allShowsByOrder())
    }

    @Post("/shows")
    fun saveShow(@Body showRequest: CreateShowRequest): HttpResponse<Int> {
        return HttpResponse.ok(showService.save(showRequest).id)
    }

    @Get("/shows/{showId}")
    fun getAvailableSeatsOfAShow(showId: Int): HttpResponse<List<Int>>? {
        return HttpResponse.ok(showService.getAvailableSeatsOfAShow(showId))
    }
}
