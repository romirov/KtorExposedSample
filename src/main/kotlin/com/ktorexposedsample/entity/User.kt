package com.ktorexposedsample.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable

data class User (
  val name: String,
)

object UserTable: LongIdTable("users") {
  val name = varchar("name", 255)
}

class UserTableRow(id: EntityID<Long>): LongEntity(id) {
  companion object: LongEntityClass<UserTableRow> (UserTable)

  var name by UserTable.name

  fun toUser() = User( name )
}