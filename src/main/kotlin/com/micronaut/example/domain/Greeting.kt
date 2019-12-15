package com.micronaut.example.domain

import javax.persistence.*

@Entity
@Table(name="greetings")
data class Greeting(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        var username: String,
        var greeting: String
)