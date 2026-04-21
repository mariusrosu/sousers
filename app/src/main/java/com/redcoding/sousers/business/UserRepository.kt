package com.redcoding.sousers.business

import com.redcoding.sousers.business.model.User

interface UserRepository {

    suspend fun getTopUsers(): Result<List<User>>
}
