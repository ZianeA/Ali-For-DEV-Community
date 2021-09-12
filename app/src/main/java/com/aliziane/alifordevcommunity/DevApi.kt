package com.aliziane.alifordevcommunity

import retrofit2.http.GET
import retrofit2.http.Path

interface DevApi {
    @GET("articles")
    suspend fun getArticles(): List<Article>

    @GET("articles/{id}")
    suspend fun getArticleById(@Path("id") id: Long): ArticleDetail
}