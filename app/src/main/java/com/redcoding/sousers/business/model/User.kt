package com.redcoding.sousers.business.model

data class User(
    val id: Long,
    val name: String,
    val profilePictureUrl: String,
    val reputation: Int,
    val isFollowed: Boolean = false,
)
