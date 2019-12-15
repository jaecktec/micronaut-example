package com.micronaut.example.dataloader

import com.micronaut.example.domain.Greeting
import com.micronaut.example.repository.GreetingsRepository
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.event.ApplicationStartupEvent

class DummyDataLoader(private val greetingsRepository: GreetingsRepository) : ApplicationEventListener<ApplicationStartupEvent> {
    override fun onApplicationEvent(event: ApplicationStartupEvent) {
        greetingsRepository.save(Greeting(null, "Bob", "Hello Bob"))
    }
}