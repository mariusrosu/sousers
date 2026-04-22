package com.redcoding.sousers.ui

import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.ui.components.InlineButtonState
import com.redcoding.sousers.ui.components.UserCardState
import com.redcoding.sousers.ui.util.Lce
import com.redcoding.sousers.ui.util.MainDispatcherRule
import com.redcoding.sousers.ui.util.StringData
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class UsersListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userRepository: UserRepository = mockk()

    private lateinit var viewModel: UsersListViewModel

    @Test
    fun `Should emit loading state when view model is initialized`() {
        coEvery { userRepository.getTopUsers() } coAnswers { awaitCancellation() }

        viewModel = createViewModel()

        assertEquals(viewModel.uiState.value, Lce.Loading)
    }

    @Test
    fun `Should emit empty content state when repository returns empty users`() = runTest {
        coEvery { userRepository.getTopUsers() } returns flowOf(Result.success(emptyList()))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(
            viewModel.uiState.value,
            Lce.Content(
                data = UiState(
                    toolbarTitle = StringData.ResourceString(R.string.toolbar_title),
                    users = emptyList()
                )
            )
        )
    }

    @Test
    fun `Should emit content state with user cards when repository returns users`() = runTest {
        coEvery { userRepository.getTopUsers() } returns flowOf(
            Result.success(
                listOf(
                    User(id = 1, name = "One", profilePictureUrl = "image_1", reputation = 1),
                    User(id = 2, name = "Two", profilePictureUrl = "image_2", reputation = 2),
                )
            )
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.value.shouldBeInstanceOf<Lce.Content<UiState>>().data.let { uiState ->
            assertEquals(
                uiState.toolbarTitle,
                StringData.ResourceString(R.string.toolbar_title),
            )
            uiState.users[0].shouldBeEqualToIgnoringFields(
                other = UserCardState(
                    profilePictureUrl = "image_1",
                    title = StringData.PlainString("One"),
                    reputation = StringData.ResourceString(R.string.reputation, 1),
                    buttonState = InlineButtonState(StringData.ResourceString(R.string.follow)) {},
                ),
                property = UserCardState::buttonState,
            )
            uiState.users[1].shouldBeEqualToIgnoringFields(
                other = UserCardState(
                    profilePictureUrl = "image_2",
                    title = StringData.PlainString("Two"),
                    reputation = StringData.ResourceString(R.string.reputation, 2),
                    buttonState = InlineButtonState(StringData.ResourceString(R.string.follow)) {},
                ),
                property = UserCardState::buttonState,
            )
        }
    }

    @Test
    fun `Should emit unknown error state when repository throws error without message`() = runTest {
        coEvery { userRepository.getTopUsers() } returns flowOf(Result.failure(Throwable()))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(
            viewModel.uiState.value,
            Lce.Error(message = StringData.ResourceString(R.string.unknown_error)),
        )
    }

    @Test
    fun `Should emit error state when repository throws error with message`() = runTest {
        val errorMessage = "Failed to fetch users"
        coEvery { userRepository.getTopUsers() } returns flowOf(
            Result.failure(Throwable(errorMessage))
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(
            viewModel.uiState.value,
            Lce.Error(message = StringData.PlainString(errorMessage)),
        )
    }

    @Test
    fun `Should follow user when follow button is clicked and user is not followed`() = runTest {
        val user = User(
            id = 1,
            name = "One",
            profilePictureUrl = "image_1",
            reputation = 1,
            isFollowed = false,
        )
        coEvery { userRepository.getTopUsers() } returns flowOf(Result.success(listOf(user)))
        every { userRepository.followUser(1) } just Runs

        viewModel = createViewModel().apply {
            onUserAction(UserAction.FollowButtonClicked(user))
        }

        verify { userRepository.followUser(1) }
    }

    @Test
    fun `Should unfollow user when follow button is clicked and user is followed`() = runTest {
        val user = User(
            id = 1,
            name = "One",
            profilePictureUrl = "image_1",
            reputation = 1,
            isFollowed = true,
        )
        coEvery { userRepository.getTopUsers() } returns flowOf(Result.success(listOf(user)))
        every { userRepository.unfollowUser(1) } just Runs

        viewModel = createViewModel().apply {
            onUserAction(UserAction.FollowButtonClicked(user))
        }

        verify { userRepository.unfollowUser(1) }
    }

    private fun createViewModel() = UsersListViewModel(userRepository)
}
