package com.ktorexposedsample.config

import com.ktorexposedsample.controller.userRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.springframework.context.annotation.Configuration

@Configuration
class WebServerConfig {
    fun connect() = embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation)
        userRouting()
    }.start(wait = true)
}