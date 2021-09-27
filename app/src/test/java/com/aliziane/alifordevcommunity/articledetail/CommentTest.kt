package com.aliziane.alifordevcommunity.articledetail

import com.aliziane.alifordevcommunity.common.User
import io.kotest.matchers.shouldBe

import org.junit.Test
import java.util.*

class CommentTest {

    @Test
    fun `return reply count when depth is 0`() {
        val comment = createComment()
        comment.replyCount shouldBe 0
    }

    @Test
    fun `return reply count when depth is 1`() {
        val comment = createComment(
            id = "101",
            replies = listOf(createComment(id = "202"), createComment(id = "303"))
        )

        comment.replyCount shouldBe 2
    }

    @Test
    fun `return reply count when depth is 3`() {
        val comment = createComment(
            id = "101",
            replies = listOf(
                createComment(
                    id = "202",
                    replies = listOf(
                        createComment(id = "404", replies = listOf(createComment(id = "505")))
                    )
                ),
                createComment(id = "303")
            )
        )

        comment.replyCount shouldBe 4
    }

    private fun createComment(
        id: String = "101",
        createAt: Date = Date(),
        bodyHtml: String = "",
        author: User = User("", ""),
        replies: List<Comment> = emptyList()
    ): Comment = Comment(id, createAt, bodyHtml, author, replies)
}