package com.redcoding.sousers.data

import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.data.api.StackOverflowApi
import com.redcoding.sousers.data.model.UserDto
import com.redcoding.sousers.data.model.UsersResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

internal class UserRepositoryImplTest {

    private val stackOverflowApi: StackOverflowApi = mockk()

    private val userRepository: UserRepository = UserRepositoryImpl(stackOverflowApi)

    @Test
    fun `Should return success result with list of users when API request succeeds`() = runTest {
        coEvery { stackOverflowApi.getTopUsers() } returns UsersResponseDto(
            users = listOf(
                UserDto(id = 1, name = "One", profilePictureUrl = "image_1", reputation = 1),
                UserDto(id = 2, name = "Two", profilePictureUrl = "image_2", reputation = 2),
            )
        )

        val result = userRepository.getTopUsers()

        assertEquals(
            result,
            Result.success(
                listOf(
                    User(id = 1, name = "One", profilePictureUrl = "image_1", reputation = 1),
                    User(id = 2, name = "Two", profilePictureUrl = "image_2", reputation = 2),
                )
            )
        )
    }

    @Test
    fun `Should return failure result when API throws exception`() = runTest {
        val error = Throwable("Failed to fetch users!")
        coEvery { stackOverflowApi.getTopUsers() } throws error

        val result = userRepository.getTopUsers()

        assertEquals(result, Result.failure<Throwable>(error))
    }
}
