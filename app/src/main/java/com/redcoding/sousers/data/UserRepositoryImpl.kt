package com.redcoding.sousers.data

import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.data.api.StackOverflowApi
import com.redcoding.sousers.data.model.UserDto
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val api: StackOverflowApi,
) : UserRepository {

    override suspend fun getTopUsers(): Result<List<User>> = runCatching {
        api.getTopUsers().users.map { it.toDomain() }
    }
}

private fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    profilePictureUrl = profilePictureUrl,
    reputation = reputation,
)
