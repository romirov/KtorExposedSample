package com.ktorexposedsample

import com.ktorexposedsample.config.DbConnectionConfig
import com.ktorexposedsample.config.WebServerConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KtorExposedSampleApplication {

	init {
		val db= DbConnectionConfig()
		val webServer = WebServerConfig()
		db.connect()
		db.migrate()
		webServer.connect()
	}
}


fun main(args: Array<String>) {
	runApplication<KtorExposedSampleApplication>(*args)
}
