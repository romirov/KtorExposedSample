package com.ktorexposedsample.controller

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.springframework.stereotype.Controller

fun Application.configureRouting() {
    routing {
        get("/user") {
            //val name = call.parameters["name"]
            call.respondText("Hello everybody")
        }
    }
}