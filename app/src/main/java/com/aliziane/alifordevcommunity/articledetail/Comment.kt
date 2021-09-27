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
) {
    /**
     * Returns the total count of all the replies including indirect replies
     */
    val replyCount by lazy { countReplies(replies) }

    private fun countReplies(replies: List<Comment>): Int {
        var count = replies.size

        for (c in replies) {
            count += countReplies(c.replies)
        }

        return count
    }
}