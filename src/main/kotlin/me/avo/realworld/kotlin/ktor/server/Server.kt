package me.avo.realworld.kotlin.ktor.server

import me.avo.realworld.kotlin.ktor.auth.AuthenticationException
import me.avo.realworld.kotlin.ktor.auth.AuthorizationException
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.Setup
import me.avo.realworld.kotlin.ktor.util.serialization.register
import me.avo.realworld.kotlin.ktor.util.toJson
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.features.StatusPages
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.locations.Locations
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Routing

fun startServer() = embeddedServer(Netty, 5000) {
    Setup()
    install(CallLogging)
    install(DefaultHeaders)
    install(Locations)
    install(GsonSupport) {
        serializeNulls()
        register<LoginCredentials>()
        register<RegistrationDetails>()
        register<User>()
        register<ArticleDetails>()
        register<Profile>()
        register<ArticleBasic>()
    }

    install(StatusPages) {
        exception<Throwable> {
            val status = when (it) {
                is AuthenticationException -> it.status
                is AuthorizationException -> it.status
                else -> HttpStatusCode.InternalServerError
            }
            println(it)
            it.stackTrace.forEach(::println)
            call.response.status(status)
            call.respond(it.toJson())
        }
    }

    install(Routing) {
        setup()
    }


}.start(wait = true)

