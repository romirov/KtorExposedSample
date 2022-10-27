package com.ktorexposedsample.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KLogging
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DatabaseConnectionConfig {
    companion object: KLogging()
    fun connect(dataSource: DataSource = getDataSource()) = Database.connect(dataSource)

    private fun getDataSource(): DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        hikariConfig.driverClassName = "org.postgresql.Driver"
        hikariConfig.username = "postgres"
        hikariConfig.password = "postgres"
        return HikariDataSource(hikariConfig)
    }

    fun migrate(dataSource: DataSource = getDataSource()) {
        val flyway = Flyway.configure().dataSource(dataSource).load()
        if (flyway.migrate().success)
            logger.info("Database migration successfuly")
    }
}