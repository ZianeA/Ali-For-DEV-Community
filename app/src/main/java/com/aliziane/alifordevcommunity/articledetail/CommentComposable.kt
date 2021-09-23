package com.aliziane.alifordevcommunity.articledetail

import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.aliziane.alifordevcommunity.R
import com.aliziane.alifordevcommunity.common.fakeComment
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText

@Composable
fun Comment(modifier: Modifier = Modifier, comment: Comment) {
    Row(modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AuthorAvatar(
                modifier = Modifier.padding(top = 16.dp),
                avatarUrl = comment.author.avatarUrl
            )
            Spacer(modifier = Modifier.height(8.dp))
            FoldButton { /*TODO*/ }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.18f),
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AuthorAndPublishDate(
                        Modifier
                            .weight(1f)
                            .padding(start = 16.dp, top = 16.dp),
                        comment.createdAt.time,
                        comment.author.name
                    )
                    Row(horizontalArrangement = Arrangement.End) {
                        MoreButton(Modifier.padding(4.dp)) { /*TODO*/ }
                    }
                }

                Body(Modifier.padding(horizontal = 16.dp), comment.bodyHtml.trim())
            }

            Spacer(modifier = Modifier.height(8.dp))

            Interaction(
                modifier = Modifier.padding(start = 8.dp),
                onLike = { /*TODO*/ },
                onReply = { /*TODO*/ })
        }
    }
}

@Composable
private fun Body(modifier: Modifier = Modifier, bodyHtml: String) {
    MaterialRichText(modifier = modifier) {
        Markdown(bodyHtml)
    }
}

@Composable
private fun MoreButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SmallIconButton(modifier = modifier, onClick = onClick) {
        Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "More options")
    }
}

@Composable
private fun AuthorAndPublishDate(modifier: Modifier, time: Long, authorName: String) {
    Row(modifier = modifier) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = authorName, fontWeight = FontWeight.Medium)
            Text(text = "•", modifier = Modifier.padding(horizontal = 8.dp))
            Text(text = DateUtils.getRelativeTimeSpanString(time).toString())
        }
    }
}

@Composable
private fun Interaction(modifier: Modifier = Modifier, onLike: () -> Unit, onReply: () -> Unit) {
    Row(modifier = modifier) {
        TextButton(onClick = onLike) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Reaction"
            )
            Text(
                modifier = modifier,
                text = "Like"
            )
        }
        TextButton(modifier = modifier, onClick = onReply) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Reply"
            )
            Text(
                modifier = modifier,
                text = "Reply"
            )
        }
    }
}

@Composable
private fun FoldButton(onClick: () -> Unit) {
    SmallIconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.UnfoldLess,
            contentDescription = "Fold comment",
            tint = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun AuthorAvatar(modifier: Modifier, avatarUrl: String) {
    Image(
        modifier = modifier
            .size(32.dp)
            .clip(shape = CircleShape)
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.18f)),
        painter = rememberImagePainter(avatarUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_person)
        },
        contentDescription = "Author Avatar",
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
private fun CommentPreview() {
    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            Comment(comment = fakeComment)
        }
    }
}

@Composable
fun SmallIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = RippleRadius)
            )
            .then(IconButtonSizeModifier),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
    }
}

private val RippleRadius = 20.dp
private val IconButtonSizeModifier = Modifier.size(40.dp)