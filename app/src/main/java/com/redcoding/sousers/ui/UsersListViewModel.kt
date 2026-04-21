package com.redcoding.sousers.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redcoding.sousers.R
import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.ui.components.UserCardState
import com.redcoding.sousers.ui.util.Lce
import com.redcoding.sousers.ui.util.asPlainString
import com.redcoding.sousers.ui.util.asResourceString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UsersListViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<Lce<UiState>>(Lce.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getTopUsers().fold(
                onSuccess = ::onTopUsersSuccess,
                onFailure = ::onTopUsersFailure,
            )
        }
    }

    private fun onTopUsersSuccess(users: List<User>) {
        _uiState.update {
            Lce.Content(UiState(users.toUserCardStates()))
        }
    }

    private fun onTopUsersFailure(throwable: Throwable) {
        _uiState.update {
            Lce.Error(
                throwable.message?.asPlainString() ?: R.string.unknown_error.asResourceString()
            )
        }
    }
}

private fun List<User>.toUserCardStates(): List<UserCardState> = map {
    UserCardState(
        profilePictureUrl = it.profilePictureUrl,
        title = it.name.asPlainString(),
        reputation = R.string.reputation.asResourceString(it.reputation),
    )
}

@Immutable
internal data class UiState(val users: List<UserCardState>)
