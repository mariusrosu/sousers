package com.redcoding.sousers.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class FollowedUsersDataStore @Inject constructor(
    private val keyValueStorage: KeyValueStorage,
) {

    private val _followedUserIds = MutableStateFlow(getPersistedIds())
    val followedUserIds: StateFlow<Set<Long>> = _followedUserIds.asStateFlow()

    fun follow(userId: Long) = updateIds { it + userId.toString() }

    fun unfollow(userId: Long) = updateIds { it - userId.toString() }

    private fun getPersistedIds(): Set<Long> =
        keyValueStorage.getStringSet(KEY_FOLLOWED_USERS)
            .map { it.toLong() }
            .toSet()

    private fun updateIds(update: (Set<String>) -> Set<String>) {
        val current = keyValueStorage.getStringSet(KEY_FOLLOWED_USERS)
        val updated = update(current)
        keyValueStorage.putStringSet(KEY_FOLLOWED_USERS, updated)
        _followedUserIds.value = updated.map { it.toLong() }.toSet()
    }
}

private const val KEY_FOLLOWED_USERS = "followed_users"
