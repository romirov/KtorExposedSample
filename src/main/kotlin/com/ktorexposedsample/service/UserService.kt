package com.ktorexposedsample.service

import com.ktorexposedsample.entity.User
import com.ktorexposedsample.entity.UserTable
import com.ktorexposedsample.entity.UserTableRow
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class UserService {

  fun add(user: User) = transaction {
    addLogger(StdOutSqlLogger)
    UserTableRow.new {
      name = user.name
    }.toUser()
  }

  fun findById(id: Long) = transaction{
    addLogger(StdOutSqlLogger)
    UserTableRow.findById(id)?.toUser()
  }

  fun findAll() = transaction {
    addLogger(StdOutSqlLogger)
    UserTableRow.all().map { it.toUser() }
  }

  fun updateById(id: Long, user: User) = transaction {
    addLogger(StdOutSqlLogger)
    val userFromDb = UserTableRow.findById(id)
    userFromDb?.name = user.name
    userFromDb?.toUser()
  }

  fun deleteById(id: Long) = transaction {
    UserTableRow.findById(id)?.let {
      it.delete()
      return@transaction true
    }
     return@transaction false
  }

  fun deleteAll() = transaction {
    UserTable.deleteAll()
  }
}