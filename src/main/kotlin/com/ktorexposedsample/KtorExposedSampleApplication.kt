package com.ktorexposedsample

import com.ktorexposedsample.config.DbConnectionConfig
import com.ktorexposedsample.config.WebServerConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KtorExposedSampleApplication {
	@Autowired
	private lateinit var db: DbConnectionConfig
	@Autowired
	private lateinit var webServer: WebServerConfig

	init {
		db.connect()
		webServer.connect()
	}
}


fun main(args: Array<String>) {
	runApplication<KtorExposedSampleApplication>(*args)
}
