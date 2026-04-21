package com.redcoding.sousers.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UsersResponseDto(
    @SerialName("items") val users: List<UserDto>,
)