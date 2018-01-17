package me.avo.realworld.kotlin.ktor.data

data class ArticleBasic(val title: String , val body: String , val description: String) {

    fun CompareAndUpdate(article : ArticleDetails): ArticleDetails {
        var newArt : ArticleDetails = article
        when {
            title.isNotEmpty() or article.title.equals(title).not() ->  newArt = article.copy(title = title)
            body.isNotEmpty() or article.body.equals(body).not() ->  newArt = article.copy(body = body)
            description.isNotEmpty() or article.description.equals(description).not() ->  newArt = article.copy(description = description)
        }
        return newArt
    }
}