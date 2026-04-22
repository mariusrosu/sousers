package com.redcoding.sousers.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("user_id") val id: Long,
    @SerialName("display_name") val name: String,
    @SerialName("profile_image") val profilePictureUrl: String,
    @SerialName("reputation") val reputation: Int,
)
