package com.redcoding.sousers.data.api

import com.redcoding.sousers.data.model.UserDetailsResponseDto
import com.redcoding.sousers.data.model.UsersResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

internal interface StackOverflowApi {

    @GET("users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow")
    suspend fun getTopUsers(): UsersResponseDto

    @GET("users/{userId}?site=stackoverflow")
    suspend fun getUserDetails(@Path("userId") userId: Long): UserDetailsResponseDto
}
