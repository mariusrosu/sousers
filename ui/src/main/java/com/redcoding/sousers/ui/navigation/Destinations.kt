package com.redcoding.sousers.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
internal object UserListDestination

@Serializable
internal data class UserDetailsDestination(val userId: Long)
