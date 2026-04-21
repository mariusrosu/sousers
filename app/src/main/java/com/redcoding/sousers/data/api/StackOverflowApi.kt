package com.redcoding.sousers.data.api

import retrofit2.http.GET

internal interface StackOverflowApi {

    @GET("users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow")
    suspend fun getTopUsers(): UsersResponse
}
