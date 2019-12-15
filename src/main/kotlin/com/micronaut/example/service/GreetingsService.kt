package com.micronaut.example.service

import com.micronaut.example.repository.GreetingsRepository
import javax.inject.Singleton

@Singleton
class GreetingsService(
        private val greetingsRepository: GreetingsRepository
){
    fun getGreetings(username: String) : List<String>{
        return greetingsRepository.findByUsername(username).map { it.greeting }
    }
}