package com.aliziane.alifordevcommunity

import retrofit2.http.GET

interface DevApi {
    @GET("articles")
    suspend fun getArticles(): List<Article>
}