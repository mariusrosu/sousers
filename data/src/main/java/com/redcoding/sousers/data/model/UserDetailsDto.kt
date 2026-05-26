package com.redcoding.sousers.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserDetailsDto(
    @SerialName("user_id") val id: Long,
    @SerialName("display_name") val name: String,
    @SerialName("profile_image") val profilePictureUrl: String,
    @SerialName("reputation") val reputation: Int,
    @SerialName("location") val location: String? = null,
    @SerialName("website_url") val websiteUrl: String? = null,
    @SerialName("badge_counts") val badgeCounts: BadgeCountsDto = BadgeCountsDto(),
)

@Serializable
internal data class BadgeCountsDto(
    @SerialName("gold") val gold: Int = 0,
    @SerialName("silver") val silver: Int = 0,
    @SerialName("bronze") val bronze: Int = 0,
)

@Serializable
internal data class UserDetailsResponseDto(
    @SerialName("items") val users: List<UserDetailsDto>,
)
