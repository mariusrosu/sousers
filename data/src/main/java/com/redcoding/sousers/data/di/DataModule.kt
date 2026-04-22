package com.redcoding.sousers.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.data.UserRepositoryImpl
import com.redcoding.sousers.data.api.StackOverflowApi
import com.redcoding.sousers.data.local.KeyValueStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideKeyValueStorage(sharedPreferences: SharedPreferences) = object : KeyValueStorage {
        override fun getStringSet(key: String): Set<String> =
            sharedPreferences.getStringSet(key, emptySet())?.toSet() ?: emptySet()

        override fun putStringSet(key: String, values: Set<String>) {
            sharedPreferences.edit { putStringSet(key, values) }
        }
    }
}

internal const val STACK_EXCHANGE_BASE_URL = "https://api.stackexchange.com/2.2/"

private const val SHARED_PREFERENCES_NAME = "stack_overflow_users_preferences"
