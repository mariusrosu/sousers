package com.redcoding.sousers.ui.userdetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.redcoding.sousers.business.UserRepository
import com.redcoding.sousers.business.model.UserDetails
import com.redcoding.sousers.ui.R
import com.redcoding.sousers.ui.components.InlineButtonState
import com.redcoding.sousers.ui.navigation.UserDetailsDestination
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

@HiltViewModel
internal class UserDetailsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val userId: Long = savedStateHandle.toRoute<UserDetailsDestination>().userId

    private val _uiState = MutableStateFlow<Lce<UiState>>(Lce.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUserDetails(userId).collect {
                it.fold(
                    onSuccess = ::onUserDetailsSuccess,
                    onFailure = ::onUserDetailsFailure,
                )
            }
        }
    }

    private fun onUserDetailsSuccess(userDetails: UserDetails) {
        _uiState.update {
            Lce.Content(
                UiState(
                    toolbarTitle = R.string.user_details_toolbar_title.asResourceString(),
                    profilePictureUrl = userDetails.profilePictureUrl,
                    name = userDetails.name.asPlainString(),
                    reputation = R.string.reputation.asResourceString(userDetails.reputation),
                    followButtonState = InlineButtonState(
                        text = userDetails.getFollowButtonText(),
                        onClick = { onUserAction(UserAction.FollowButtonClicked(userDetails)) },
                    ),
                    location = userDetails.location?.let { R.string.user_location.asResourceString(it) },
                    websiteUrl = userDetails.websiteUrl?.let { R.string.user_website.asResourceString(it) },
                    badgeCounts = BadgeCountsUiState(
                        gold = R.string.badge_gold.asResourceString(userDetails.badgeCounts.gold),
                        silver = R.string.badge_silver.asResourceString(userDetails.badgeCounts.silver),
                        bronze = R.string.badge_bronze.asResourceString(userDetails.badgeCounts.bronze),
                    ),
                )
            )
        }
    }

    private fun onUserDetailsFailure(throwable: Throwable) {
        _uiState.update {
            Lce.Error(
                throwable.message?.asPlainString() ?: R.string.unknown_error.asResourceString()
            )
        }
    }

    internal fun onUserAction(action: UserAction) {
        when (action) {
            is UserAction.FollowButtonClicked -> onFollowButtonClicked(action.userDetails)
        }
    }

    private fun onFollowButtonClicked(userDetails: UserDetails) {
        if (userDetails.isFollowed) {
            userRepository.unfollowUser(userDetails.id)
        } else {
            userRepository.followUser(userDetails.id)
        }
    }
}

private fun UserDetails.getFollowButtonText(): StringData = if (isFollowed) {
    R.string.unfollow.asResourceString()
} else {
    R.string.follow.asResourceString()
}

@Immutable
internal data class UiState(
    val toolbarTitle: StringData,
    val profilePictureUrl: String,
    val name: StringData,
    val reputation: StringData,
    val followButtonState: InlineButtonState,
    val location: StringData?,
    val websiteUrl: StringData?,
    val badgeCounts: BadgeCountsUiState,
)

@Immutable
internal data class BadgeCountsUiState(
    val gold: StringData,
    val silver: StringData,
    val bronze: StringData,
)

internal sealed interface UserAction {
    data class FollowButtonClicked(val userDetails: UserDetails) : UserAction
}
