package com.ktorexposedsample.service

import com.ktorexposedsample.PosgresqlTestcontainerWrapper
import com.ktorexposedsample.config.DatabaseConnectionConfig
import com.ktorexposedsample.entity.User
import com.ktorexposedsample.entity.UserDto
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.stereotype.Component
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
  init {
    PosgresqlTestcontainerWrapper.start()
    val dataSource = PosgresqlTestcontainerWrapper.getDataSource()
    val databaseConnectionConfig = DatabaseConnectionConfig()
    databaseConnectionConfig.connect(dataSource)
    databaseConnectionConfig.migrate(dataSource)
  }

  val userService = UserService()

  private fun addUser(nameUser: String): UserDto {
    return userService.add(User(nameUser))
  }
  @BeforeAll
  fun addTest(){
    //given
    val userName = "addTest"
    //when
    val userFromDb = addUser(userName)
    //then
    assertNotNull(userFromDb)
    assertEquals(userFromDb.name, userName)
  }

  @Test
  fun findByIdTest() {
    //given
    val userName = "findByIdTest"
    //when
    val userId = addUser(userName).id
    assertEquals( userService.findById(userId)?.name, userName )
  }

  @Test
  fun findByIdAllTest() {
    assertTrue(userService.findAll().size > 0)
  }

  @Test
  fun updateByIdTest() {
    val userName = "updateByIdTest"
    //when
    val userId = addUser(userName).id
    val userNameNew = "updateByIdTestNew"
    val userFromDb = userService.updateById(userId, User(userNameNew))
    assertNotNull(userFromDb)
    assertEquals( userFromDb.id, userId )
    assertEquals( userFromDb.name, userNameNew )
  }

  @Test
  fun deleteByIdTest() {
    val userName = "deleteByIdTest"
    //when
    val userId = addUser(userName).id
    assertEquals( userService.deleteById(userId), true )
  }

  @AfterAll
  fun deleteAllTest(){
    assertTrue { userService.deleteAll() > 0 }
  }
}