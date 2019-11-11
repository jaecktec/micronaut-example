package com.micronaut.example

import io.micronaut.runtime.Micronaut

object App {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.micronaut.example")
                .mainClass(App::class.java)
                .start()
    }
}