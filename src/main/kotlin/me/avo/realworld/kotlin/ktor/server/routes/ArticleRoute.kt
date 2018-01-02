package me.avo.realworld.kotlin.ktor.server.routes

import me.avo.realworld.kotlin.ktor.data.Article
import me.avo.realworld.kotlin.ktor.data.ArticleQuery
import me.avo.realworld.kotlin.ktor.persistence.ArticleSource
import me.avo.realworld.kotlin.ktor.persistence.ArticleSourceImpl
import me.avo.realworld.kotlin.ktor.server.optionalLogin
import me.avo.realworld.kotlin.ktor.server.requireLogin
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.*



    fun Route.article() = route("articles") {
        val articleSource: ArticleSource = ArticleSourceImpl()

        get {
            val user = optionalLogin()
            val query = ArticleQuery.fromParameter(call.parameters)
            val articles = articleSource.getArticles(query)
            call.respond(articles)
        }

        get("feed") {
            val user = requireLogin()
            TODO("Feed Articles")
        }


        post {
            val user = requireLogin()
            val details = call.receive<Article>()
            val article = articleSource.insertArticle(user, details)
            call.respond(article)
        }

        route("{slug}") {

            get {
               val slug = call.parameters["slug"]
                if(slug != null) {
                    val articles = articleSource.getArticle(slug)
                    call.respond(articles)
                }
            }

            put {
                requireLogin()
                TODO("Update ArticleDetails")
            }

            delete {
                requireLogin()
                TODO("Delete ArticleDetails")
            }

            route("comments") {
                post {
                    requireLogin()
                    TODO("Add Comments to an ArticleDetails")
                }

                get {
                    optionalLogin()
                    TODO("Get Comments from an ArticleDetails")
                }

                delete("{id}") {
                    requireLogin()
                    TODO("Delete Comment")
                }

            }

            route("favorite") {
                post {
                    requireLogin()
                    TODO("Favorite ArticleDetails")
                }

                delete {
                    requireLogin()
                    TODO("Unfavorite ArticleDetails")
                }
            }

        }

    }
