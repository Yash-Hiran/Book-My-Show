package com.demo.book.api

import com.demo.book.show.service.ShowService
import com.demo.book.show.request.CreateShowRequest
import com.demo.book.show.entity.Show
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
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

    @Get(uris = ["shows/{showId}"])
    fun getShow(showId: Int): HttpResponse<Show> {
        return HttpResponse.ok(showService.showById(showId))
    }

    @Post("/shows")
    fun saveShow(@Body showRequest: CreateShowRequest): HttpResponse<Int> {
        return HttpResponse.ok(showService.save(showRequest).id)
    }

    @Put(uris = ["/shows/{showId}/price/{price}"])
    fun addPrice(showId: Int, price: Int): HttpResponse<Unit> {
        return HttpResponse.ok(showService.updatePrice(showId, price))
    }
}
