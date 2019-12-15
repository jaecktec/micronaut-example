package com.micronaut.example.dataloader

import com.micronaut.example.domain.Greeting
import com.micronaut.example.repository.GreetingsRepository
import io.micronaut.runtime.event.ApplicationStartupEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DummyDataLoaderTest {
    lateinit var dummyDataLoader: DummyDataLoader
    lateinit var greetingsRepository: GreetingsRepository
    @BeforeEach
    fun setUp() {
        greetingsRepository = mockk()
        dummyDataLoader = DummyDataLoader(greetingsRepository)
    }
    @Test
    fun `adds data to the repository`() {
        // given
        every { greetingsRepository.save(any<Greeting>()) } returns Greeting(1, "Bob", "Hello Bob")
        val applicationStartupEvent = ApplicationStartupEvent(mockk())
        // when
        dummyDataLoader.onApplicationEvent(applicationStartupEvent)
        verify { greetingsRepository.save(Greeting(null, "Bob", "Hello Bob")) }
    }
}