package com.redcoding.sousers.business

import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.business.model.UserDetails
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getTopUsers(): Flow<Result<List<User>>>

    fun getUserDetails(userId: Long): Flow<Result<UserDetails>>

    fun followUser(userId: Long)

    fun unfollowUser(userId: Long)
}
