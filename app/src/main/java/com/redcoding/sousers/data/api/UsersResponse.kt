package com.redcoding.sousers.data.api

import com.redcoding.sousers.data.model.User
import kotlinx.serialization.Serializable

@Serializable
internal data class UsersResponse(
    val items: List<User>,
)
