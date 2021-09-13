package com.aliziane.alifordevcommunity.home

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Article(
    val id: Long,
    val title: String,
    val description: String,
    @SerialName("comments_count") val commentCount: Int,
    @SerialName("public_reactions_count") val reactionCount: Int,
    @SerialName("cover_image") val coverImageUrl: String?,
    @SerialName("reading_time_minutes") val readTimeInMinutes: Int,
    val url: String,
    @SerialName("canonical_url") val canonicalUrl: String,
    @SerialName("published_at") @Contextual val publishedAt: Date,
    @SerialName("edited_at") @Contextual val editedAt: Date?,
    @SerialName("tag_list") val tags: List<String>,
    @SerialName("user") val author: User
) {
    @Serializable
    data class User(val name: String, @SerialName("profile_image") val avatarUrl: String)
}