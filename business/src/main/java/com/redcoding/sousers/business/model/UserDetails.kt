package com.redcoding.sousers.business.model

data class UserDetails(
    val id: Long,
    val name: String,
    val profilePictureUrl: String,
    val reputation: Int,
    val isFollowed: Boolean = false,
    val location: String?,
    val websiteUrl: String?,
    val badgeCounts: BadgeCounts,
)

data class BadgeCounts(
    val gold: Int,
    val silver: Int,
    val bronze: Int,
)
