package com.redcoding.sousers.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.ui.components.InlineButtonState
import com.redcoding.sousers.ui.components.UserCardState
import com.redcoding.sousers.ui.util.Lce
import com.redcoding.sousers.ui.util.StringData
import com.redcoding.sousers.ui.util.asPlainString
import com.redcoding.sousers.ui.util.asResourceString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
internal class UsersListViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<Lce<UiState>>(Lce.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getTopUsers().collect {
                it.fold(
                    onSuccess = ::onTopUsersSuccess,
                    onFailure = ::onTopUsersFailure,
                )
            }
        }
    }

    private fun onTopUsersSuccess(users: List<User>) {
        _uiState.update {
            Lce.Content(
                UiState(
                    toolbarTitle = R.string.toolbar_title.asResourceString(),
                    users = createUserCardStates(users),
                )
            )
        }
    }

    private fun onTopUsersFailure(throwable: Throwable) {
        _uiState.update {
            Lce.Error(
                throwable.message?.asPlainString() ?: R.string.unknown_error.asResourceString()
            )
        }
    }

    private fun createUserCardStates(users: List<User>): List<UserCardState> = users.map { user ->
        UserCardState(
            profilePictureUrl = user.profilePictureUrl,
            title = user.name.asPlainString(),
            reputation = user.getReputationText(),
            buttonState = InlineButtonState(
                text = user.getFollowButtonText(),
                onClick = { onUserAction(UserAction.FollowButtonClicked(user)) },
            ),
        )
    }

    internal fun onUserAction(action: UserAction) {
        when (action) {
            is UserAction.FollowButtonClicked -> onFollowButtonClicked(action.user)
        }
    }

    private fun onFollowButtonClicked(user: User) {
        if (user.isFollowed) {
            userRepository.unfollowUser(user.id)
        } else {
            userRepository.followUser(user.id)
        }
    }
}

private fun User.getReputationText(): StringData = R.string.reputation.asResourceString(reputation)

private fun User.getFollowButtonText(): StringData = if (isFollowed) {
    R.string.unfollow.asResourceString()
} else {
    R.string.follow.asResourceString()
}

@Immutable
internal data class UiState(
    val toolbarTitle: StringData,
    val users: List<UserCardState>,
)

internal sealed interface UserAction {
    data class FollowButtonClicked(val user: User) : UserAction
}
