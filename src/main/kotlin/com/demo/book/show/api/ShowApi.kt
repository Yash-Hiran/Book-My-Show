package com.demo.book.api

import com.demo.book.show.service.ShowService
import com.demo.book.show.request.CreateShowRequest
import com.demo.book.show.entity.Show
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import javax.inject.Inject

@Controller
class ShowApi(@Inject val showService: ShowService) {

    @Get("/shows")
    fun allShows(): HttpResponse<List<Show>> {
        return HttpResponse.ok(showService.allShows())
    }

    @Post("/shows")
    fun saveShow(@Body showRequest: CreateShowRequest): HttpResponse<Int> {
        return HttpResponse.ok(showService.save(showRequest).id)
    }
}
