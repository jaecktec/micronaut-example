package com.micronaut.example.controller

import com.micronaut.example.service.GreetingsService
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/greetings")
class GreetingsController(
        private val greetingsService: GreetingsService
) {

    @Get
    fun getGreetings(): GreetingsResponse {
        return greetingsService.getGreetings("Bob").run { GreetingsResponse(this) }
    }

    @Introspected
    data class GreetingsResponse(val greetings: List<String>)
}
