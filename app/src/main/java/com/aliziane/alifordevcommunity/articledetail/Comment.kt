package com.aliziane.alifordevcommunity.articledetail

import com.aliziane.alifordevcommunity.common.User
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Comment(
    @SerialName("id_code") val id: String,
    @SerialName("created_at") @Contextual val createdAt: Date,
    @SerialName("body_html") val bodyHtml: String,
    @SerialName("user") val author: User,
    @SerialName("children") val replies: List<Comment>
)