package com.redcoding.sousers.business

import com.redcoding.sousers.business.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getTopUsers(): Flow<Result<List<User>>>

    fun followUser(userId: Long)

    fun unfollowUser(userId: Long)
}
