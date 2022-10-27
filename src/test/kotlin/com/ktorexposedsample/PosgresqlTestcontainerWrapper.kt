package com.ktorexposedsample

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource

object PosgresqlTestcontainerWrapper {

    private val postgresTestContainer =  PostgreSQLContainer("postgres:14-alpine").apply {
      withDatabaseName("postgres")
      withUsername("postgres")
      withPassword("postgres")
      start()
    }

  fun start() = postgresTestContainer.start()

  fun getDataSource(): DataSource{
    val hikariConfig = HikariConfig()
    hikariConfig.jdbcUrl = postgresTestContainer.getJdbcUrl()
    hikariConfig.username = postgresTestContainer.getUsername()
    hikariConfig.password = postgresTestContainer.getPassword()
    return HikariDataSource(hikariConfig)
  }
}