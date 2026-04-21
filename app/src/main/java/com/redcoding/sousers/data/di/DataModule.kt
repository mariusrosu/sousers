package com.redcoding.sousers.data.di

import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.data.UserRepositoryImpl
import com.redcoding.sousers.data.api.StackOverflowApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Singleton
    @Provides
    fun provideRetrofit(json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(STACK_EXCHANGE_BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Singleton
    @Provides
    fun provideStackOverflowService(retrofit: Retrofit): StackOverflowApi =
        retrofit.create(StackOverflowApi::class.java)

    @Provides
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl
}

private const val STACK_EXCHANGE_BASE_URL = "http://api.stackexchange.com/2.2"
