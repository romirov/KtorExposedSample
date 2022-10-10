package com.ktorexposedsample.config

import mu.KLogging
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConnectionConfig {
    companion object: KLogging()

    private val url = "jdbc:postgresql://localhost:5432/postgres"
    private val driver = "org.postgresql.Driver"
    private val user = "postgres"
    private val password = "postgres"

    fun connect() = Database.connect(url
        ,driver
        ,user
        ,password)

    fun migrate() {
        val flyway = Flyway.configure().dataSource(url, user, password).load()
        if (flyway.migrate().success)
            logger.info("Database migration successfuly")
    }
}