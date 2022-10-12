package com.ktorexposedsample.controller

import com.ktorexposedsample.entity.User
import com.ktorexposedsample.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    val userService = UserService()
    routing {
        get("/user") {
            val users = userService.findAll()
            call.respond(users.toString())
        }

        get("/user/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val user = userService.findById(id.toLong()) ?: return@get call.respondText(
                "Not Found",
                status = HttpStatusCode.NotFound
            )
            call.respondText(user.toString())
        }

        post("/user/add") {
            val name = call.parameters["name"] ?: return@post call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            userService.add(User(name))
            call.respondText("User added", status = HttpStatusCode.OK)
        }

        put("/user/update/{id?}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val name = call.parameters["name"]?: return@put call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            userService.updateById(id.toLong(), User(name))
            call.respondText("User updated", status = HttpStatusCode.OK)
        }

        delete("/user/delete") {
            userService.deleteAll()
            call.respondText("Delete all users", status = HttpStatusCode.OK)
        }

        delete("/user/delete/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            userService.deleteById(id.toLong())
            call.respondText("Delete user with id $id", status = HttpStatusCode.OK)
        }
    }
}