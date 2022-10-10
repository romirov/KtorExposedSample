package com.ktorexposedsample

import com.ktorexposedsample.config.DatabaseConnectionConfig
import com.ktorexposedsample.config.WebServerConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KtorExposedSampleApplication {

	init {
		val db= DatabaseConnectionConfig()
		val webServer = WebServerConfig()
		db.connect()
		db.migrate()
		webServer.connect()
	}
}


fun main(args: Array<String>) {
	runApplication<KtorExposedSampleApplication>(*args)
}
