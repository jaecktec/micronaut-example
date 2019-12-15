package com.micronaut.example.controller

import com.micronaut.example.service.GreetingsService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

@Client("/greetings")
interface GreetingsControllerTestClient {
    @Get
    fun getGreetings(): HttpResponse<Greetings>

    data class Greetings(val greetings: List<String>)
}

@MicronautTest
class GreetingsControllerTest {

    @MockBean(GreetingsService::class)
    fun greetingsService(): GreetingsService = mockk()

    @Inject
    lateinit var client: GreetingsControllerTestClient

    @Inject
    lateinit var greetingsService: GreetingsService

    @Test
    fun `should return greetings`() {
        // given
        every { greetingsService.getGreetings(any()) } returns listOf("Hey Bob")

        // when
        val response = client.getGreetings()

        // then
        assertEquals(HttpStatus.OK, response.status())
        assertEquals(listOf("Hey Bob"), response.body()!!.greetings)
        verify { greetingsService.getGreetings("Bob") }
    }
}