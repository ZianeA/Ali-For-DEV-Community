package com.aliziane.alifordevcommunity.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, @SerialName("profile_image") val avatarUrl: String)