package com.ktorexposedsample.config

import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DbConnectionConfig (
) {
    @Autowired
    private lateinit var dataSource: DataSource

    fun connect() = Database.connect(dataSource)
}