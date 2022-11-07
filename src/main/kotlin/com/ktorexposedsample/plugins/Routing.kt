package com.ktorexposedsample.plugins

import com.ktorexposedsample.entity.User
import com.ktorexposedsample.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    configureSerialization()
    val userService = UserService()
    routing {
        route("/user") {
            get {
                val users = userService.findAll()
                call.respond(users)
            }

            get("/{id?}") {
                val id = call.parameters["id"]
                if(id.isNullOrEmpty() || id.isBlank()) {
                    return@get call.respond(HttpStatusCode.BadRequest, "Missing id")
                }
                val user = userService.findById(id.toLong())
                if (user == null){
                    return@get call.respond( HttpStatusCode.NotFound, "Not Found")
                }
                call.respond(user)
            }

            post("/add") {
                val user = call.receive<User>()
                val userDto = userService.add(user)
                call.respond(userDto)
            }

            put("/update/{id?}") {
                val id = call.parameters["id"]
                if(id.isNullOrEmpty() || id.isBlank()) {
                    return@put call.respond(HttpStatusCode.BadRequest, "Missing id")
                }
                val user = call.receive<User>()
                val userDto = userService.updateById(id.toLong(), user)
                if (userDto == null){
                    return@put call.respond( HttpStatusCode.NotFound, "Not Found")
                }
                call.respond(userDto)
            }

            delete("/delete") {
                userService.deleteAll()
                call.respondText("Delete all users", status = HttpStatusCode.OK)
            }

            delete("/delete/{id?}") {
                val id = call.parameters["id"] ?: return@delete call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                userService.deleteById(id.toLong())
                call.respondText("Delete user with id $id", status = HttpStatusCode.OK)
            }
        }
    }
}