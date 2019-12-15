package com.micronaut.example.configuration

import com.micronaut.example.dataloader.DummyDataLoader
import com.micronaut.example.repository.GreetingsRepository
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory

@Factory
class DummyDataLoaderFactory {
    @Bean
    fun dummyDataLoader(greetingsRepository: GreetingsRepository): DummyDataLoader {
        return DummyDataLoader(greetingsRepository)
    }
}