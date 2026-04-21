package com.redcoding.sousers

import com.redcoding.sousers.data.api.StackOverflowApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal class StackOverflowApiTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val api = Retrofit.Builder()
        .baseUrl("https://api.stackexchange.com/2.2/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(StackOverflowApi::class.java)

    @Test
    fun `When calling StackOverflow API should return list of users with id, name, picture and reputation`() = runTest {
        val response = api.getTopUsers()

        assert(response.items.size == 20) { "Response should return 20 users!" }
    }
}
