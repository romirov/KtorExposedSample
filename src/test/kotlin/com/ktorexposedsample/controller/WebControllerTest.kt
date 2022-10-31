package com.ktorexposedsample.controller

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebControllerTest {
    private val config = HoconApplicationConfig(ConfigFactory.load("controller.conf"))
    private val prefix = "ktor.jwt"
    private val propList =
        listOf("$prefix.domain", "$prefix.audience", "$prefix.realm", "$prefix.secret", "$prefix.validityMs")


    private fun withServer(block: TestApplicationEngine.() -> Unit) {
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                propList.forEach {
                    put(it, config.property(it).getString())
                }
            }
            configureRouting()
        }, block)
    }
}