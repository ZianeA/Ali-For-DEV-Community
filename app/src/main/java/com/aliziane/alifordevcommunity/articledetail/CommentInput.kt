package com.aliziane.alifordevcommunity.articledetail

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliziane.alifordevcommunity.R
import com.aliziane.alifordevcommunity.common.UserAvatar
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CommentInput(modifier: Modifier = Modifier, avatarUrl: String) {
    var input by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(modifier = modifier) {
        UserAvatar(avatarUrl = avatarUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.18f),
                    shape = MaterialTheme.shapes.medium
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                )
                .padding(16.dp)
        ) {
            Box {
                androidx.compose.animation.AnimatedVisibility(
                    visible = input.isEmpty(),
                    exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
                    enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End)
                ) {
                    // Placeholder
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                        Text(
                            text = stringResource(R.string.placeholder_write_comment),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }

                BasicTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.focusRequester(focusRequester),
                    textStyle = MaterialTheme.typography.subtitle1
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun CommentInputPreview() {
    AliForDEVCommunityTheme {
        Surface {
            CommentInput(
                modifier = Modifier.padding(16.dp),
                avatarUrl = ""
            )
        }
    }
}