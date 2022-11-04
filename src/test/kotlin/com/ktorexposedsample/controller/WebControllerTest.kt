package com.ktorexposedsample.controller

import com.ktorexposedsample.entity.User
import com.ktorexposedsample.entity.UserDto
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebControllerTest {
  private val config = HoconApplicationConfig(ConfigFactory.load("application.conf"))
  private val prefix = "ktor.jwt"
  private val propList =
      listOf(
          "$prefix.domain",
          "$prefix.audience",
          "$prefix.realm",
          "$prefix.secret",
          "$prefix.validityMs")
  private val user = User("Test User")

  private fun withServer(block: TestApplicationEngine.() -> Unit) {
    withTestApplication({
      (environment.config as MapApplicationConfig).apply {
        propList.forEach {
          put(it, config.property(it).getString())
        }
      }
      userRouting()
    }, block)
  }

  fun addUser(user: User) {
    var userDto: UserDto? = null
    withServer {
      handleRequest(HttpMethod.Post, "/add") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.encodeToString(user))
      }.apply {
        assertEquals(response.status(), HttpStatusCode.OK)
        userDto =
            Json.decodeFromString(response.content.toString())
        assertNotNull(userDto?.id)
        assertTrue { userDto?.id!! > 0L }
      }
    }
  }

  @Test
  fun addUserTest(){
    addUser(user)
  }
}