package com.micronaut.example.repository

import com.micronaut.example.domain.Greeting
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface GreetingsRepository : CrudRepository<Greeting, Long> {
    fun findByUsername(username: String): Collection<Greeting>
}