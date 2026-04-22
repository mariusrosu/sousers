package com.redcoding.sousers.data

import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.data.api.StackOverflowApi
import com.redcoding.sousers.data.local.FollowedUsersDataStore
import com.redcoding.sousers.data.model.UserDto
import com.redcoding.sousers.data.model.UsersResponseDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class UserRepositoryImplTest {

    private val stackOverflowApi: StackOverflowApi = mockk()

    private val followedUsersDataStore: FollowedUsersDataStore = mockk()

    private val userRepository: UserRepository = UserRepositoryImpl(
        stackOverflowApi = stackOverflowApi,
        followedUsersDataStore = followedUsersDataStore,
    )

    @Test
    fun `Should return success result with list of users when API request succeeds`() = runTest {
        coEvery { stackOverflowApi.getTopUsers() } returns UsersResponseDto(
            users = listOf(
                UserDto(id = 1, name = "One", profilePictureUrl = "image_1", reputation = 1),
                UserDto(id = 2, name = "Two", profilePictureUrl = "image_2", reputation = 2),
            )
        )
        every { followedUsersDataStore.followedUserIds } returns MutableStateFlow(emptySet())

        val result = userRepository.getTopUsers().first()

        Assert.assertEquals(
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
        every { followedUsersDataStore.followedUserIds } returns MutableStateFlow(emptySet())

        val result = userRepository.getTopUsers().first()

        Assert.assertEquals(result, Result.failure<Throwable>(error))
    }

    @Test
    fun `Should return followed user when followed user IDs contain that user`() = runTest {
        coEvery { stackOverflowApi.getTopUsers() } returns UsersResponseDto(
            users = listOf(
                UserDto(id = 1, name = "One", profilePictureUrl = "image_1", reputation = 1),
                UserDto(id = 2, name = "Two", profilePictureUrl = "image_2", reputation = 2),
            )
        )
        every { followedUsersDataStore.followedUserIds } returns MutableStateFlow(setOf(2))

        val result = userRepository.getTopUsers().first()

        Assert.assertEquals(
            result,
            Result.success(
                listOf(
                    User(
                        id = 1,
                        name = "One",
                        profilePictureUrl = "image_1",
                        reputation = 1,
                        isFollowed = false,
                    ),
                    User(
                        id = 2,
                        name = "Two",
                        profilePictureUrl = "image_2",
                        reputation = 2,
                        isFollowed = true,
                    ),
                )
            )
        )
    }

    @Test
    fun `Should store new followed user ID when user is followed`() {
        val userId = 1L
        every { followedUsersDataStore.follow(userId) } just Runs

        userRepository.followUser(userId)

        verify { followedUsersDataStore.follow(userId) }
    }

    @Test
    fun `Should remove stored followed user ID when user is unfollowed`() {
        val userId = 1L
        every { followedUsersDataStore.unfollow(userId) } just Runs

        userRepository.unfollowUser(userId)

        verify { followedUsersDataStore.unfollow(userId) }
    }
}
