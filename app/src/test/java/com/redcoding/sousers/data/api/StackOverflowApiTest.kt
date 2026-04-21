package com.redcoding.sousers.data.api

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal class StackOverflowApiTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val stackOverflowApi = Retrofit.Builder()
        .baseUrl("https://api.stackexchange.com/2.2/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(StackOverflowApi::class.java)

    @Test
    fun `Should return 20 users when calling StackOverflow API for top users`() = runTest {
            val result = stackOverflowApi.getTopUsers()

            assertEquals(result.users.size, 20)
        }
}
