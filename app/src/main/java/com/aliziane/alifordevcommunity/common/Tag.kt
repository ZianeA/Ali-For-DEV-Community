package com.aliziane.alifordevcommunity.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Tag(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    val hashColor = MaterialTheme.colors.onSurface.copy(alpha = 0.28f)
    val textColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)

    ClickableText(
        modifier = modifier,
        text = buildAnnotatedString {
            pushStyle(SpanStyle(hashColor))
            append("#")

            pop()
            pushStyle(SpanStyle(textColor))
            append(text)
        },
        style = MaterialTheme.typography.body1,
        onClick = { onClick() }
    )
}

@Composable
fun HyperlinkTag(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    ClickableText(
        modifier = modifier,
        text = AnnotatedString(text, spanStyle = SpanStyle(Color(0xFF007BFF))),
        onClick = { onClick() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Chip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        onClick = onClick,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
        )
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.body2) {
            Row(modifier = Modifier.padding(paddingValues = contentPadding)) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun TagPreview() {
    Tag(text = "Android") {}
}

@Preview
@Composable
fun HyperlinkTagPreview() {
    HyperlinkTag(text = "#Android") {}
}

@Preview
@Composable
fun ChipPreview() {
    Chip(onClick = {}) {
        Text(text = "Android")
    }
}