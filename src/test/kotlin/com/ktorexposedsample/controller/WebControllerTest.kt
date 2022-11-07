package com.ktorexposedsample.controller

import com.ktorexposedsample.PosgresqlTestcontainerWrapper
import com.ktorexposedsample.config.DatabaseConnectionConfig
import com.ktorexposedsample.entity.User
import com.ktorexposedsample.entity.UserDto
import com.ktorexposedsample.plugins.configureRouting
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.stereotype.Component
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
      configureRouting()
    }, block)
  }

  fun addUser(user: User): UserDto? {
    var userDto: UserDto? = null
    withServer {
      handleRequest(HttpMethod.Post, "/user/add") {
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
    return userDto
  }

  @BeforeAll
  fun initDatabase(){
    PosgresqlTestcontainerWrapper.start()
    val dataSource = PosgresqlTestcontainerWrapper.getDataSource()
    val db = DatabaseConnectionConfig()
    db.connect(dataSource)
    db.migrate(dataSource)
  }

  @Test
  fun addUserTest(){
    addUser(user)
  }

 @Test
 fun getUserByIdTest(){
   val userDto = addUser(user)
   assertNotNull(userDto)
   withServer {
     handleRequest(HttpMethod.Get, "/user/${userDto.id}").apply {
       assertEquals(response.status(), HttpStatusCode.OK)
       val userFromDbById =
           Json.decodeFromString<UserDto>(response.content.toString())
       assertNotNull(userFromDbById)
       assertEquals(userDto, userFromDbById)
     }
   }
 }

  @Test
  fun getAllUsersTest(){
    val userDto = addUser(user)
    assertNotNull(userDto)
    withServer {
      handleRequest(HttpMethod.Get, "/user").apply {
        assertEquals(response.status(), HttpStatusCode.OK)
        val usersFromDb =
            Json.decodeFromString<List<UserDto>>(response.content.toString())
        assertNotNull(usersFromDb.isNotEmpty())
      }
    }
  }

  @Test
  fun updateUserByIdTest(){
    val userDto = addUser(user)
    assertNotNull(userDto)
    withServer {
      handleRequest(HttpMethod.Put, "/user/update/${userDto.id}"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.encodeToString(user.copy("New User Test")))
      }.apply {
        assertEquals(response.status(), HttpStatusCode.OK)
        val usersFromDb =
            Json.decodeFromString<UserDto>(response.content.toString())
        assertNotNull(usersFromDb)
        assertEquals(usersFromDb.name, "New User Test")
      }
    }
  }

  @Test
  fun deleteUserByIdTest(){
    val userDto = addUser(user)
    assertNotNull(userDto)
    withServer {
      handleRequest(HttpMethod.Delete, "/user/delete/${userDto.id}").apply {
        assertEquals(response.status(), HttpStatusCode.OK)
      }
    }
  }

  @Test
  fun deleteAllUsersTest(){
    val userDto = addUser(user)
    assertNotNull(userDto)
    withServer {
      handleRequest(HttpMethod.Delete, "/user/delete").apply {
        assertEquals(response.status(), HttpStatusCode.OK)
      }
    }
  }
}