package com.redcoding.sousers.data

import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.business.model.BadgeCounts
import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.business.model.UserDetails
import com.redcoding.sousers.data.api.StackOverflowApi
import com.redcoding.sousers.data.local.FollowedUsersDataStore
import com.redcoding.sousers.data.model.UserDetailsDto
import com.redcoding.sousers.data.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val stackOverflowApi: StackOverflowApi,
    private val followedUsersDataStore: FollowedUsersDataStore,
) : UserRepository {

    override fun getTopUsers(): Flow<Result<List<User>>> =
        combine(
            topUsersFlow(),
            followedUsersDataStore.followedUserIds,
        ) { usersResult, followedUserIds ->
            usersResult.map { users ->
                users.map { it.toDomain(isFollowed = followedUserIds.contains(it.id)) }
            }
        }

    override fun getUserDetails(userId: Long): Flow<Result<UserDetails>> =
        combine(
            userDetailsFlow(userId),
            followedUsersDataStore.followedUserIds,
        ) { detailsResult, followedUserIds ->
            detailsResult.map { it.toDomain(isFollowed = followedUserIds.contains(it.id)) }
        }

    override fun followUser(userId: Long) {
        followedUsersDataStore.follow(userId)
    }

    override fun unfollowUser(userId: Long) {
        followedUsersDataStore.unfollow(userId)
    }

    private fun topUsersFlow(): Flow<Result<List<UserDto>>> = flow {
        emit(runCatching { stackOverflowApi.getTopUsers().users })
    }

    private fun userDetailsFlow(userId: Long): Flow<Result<UserDetailsDto>> = flow {
        emit(runCatching { stackOverflowApi.getUserDetails(userId).users.first() })
    }
}

private fun UserDto.toDomain(isFollowed: Boolean): User = User(
    id = id,
    name = name,
    profilePictureUrl = profilePictureUrl,
    reputation = reputation,
    isFollowed = isFollowed,
)

private fun UserDetailsDto.toDomain(isFollowed: Boolean): UserDetails = UserDetails(
    id = id,
    name = name,
    profilePictureUrl = profilePictureUrl,
    reputation = reputation,
    isFollowed = isFollowed,
    location = location,
    websiteUrl = websiteUrl,
    badgeCounts = BadgeCounts(
        gold = badgeCounts.gold,
        silver = badgeCounts.silver,
        bronze = badgeCounts.bronze,
    ),
)
