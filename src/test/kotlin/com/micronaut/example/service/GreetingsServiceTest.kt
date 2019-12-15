package com.micronaut.example.service

import com.micronaut.example.domain.Greeting
import com.micronaut.example.repository.GreetingsRepository
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class GreetingsServiceTest {

    @Inject
    lateinit var greetingsRepository: GreetingsRepository


    @Inject
    lateinit var subject: GreetingsService

    @BeforeEach
    fun setUp() {
        greetingsRepository.deleteAll()
    }

    @Test
    fun `should return greetings for user`() {
        // given
        val username = "Bob"
        val expectedGreetings = listOf("Hallo Bob")
        greetingsRepository.saveAll(expectedGreetings.map { s -> Greeting(null, username, s) })
        // when
        val greetings = subject.getGreetings(username)
        // then
        assertEquals(expectedGreetings, greetings)
    }
}