package com.ktorexposedsample.config

import com.ktorexposedsample.controller.userRouting
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.springframework.context.annotation.Configuration

@Configuration
class WebServerConfig {
    fun connect() = embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        userRouting()
    }.start(wait = true)
}