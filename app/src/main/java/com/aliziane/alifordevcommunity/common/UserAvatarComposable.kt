package com.aliziane.alifordevcommunity.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.aliziane.alifordevcommunity.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UserAvatar(modifier: Modifier = Modifier, avatarUrl: String) {
    Image(
        modifier = modifier
            .size(40.dp)
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