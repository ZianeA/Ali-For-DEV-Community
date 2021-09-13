package com.aliziane.alifordevcommunity.common

import com.aliziane.alifordevcommunity.articledetail.ArticleDetail
import com.aliziane.alifordevcommunity.home.Article
import retrofit2.http.GET
import retrofit2.http.Path

interface DevApi {
    @GET("articles")
    suspend fun getArticles(): List<Article>

    @GET("articles/{id}")
    suspend fun getArticleById(@Path("id") id: Long): ArticleDetail
}