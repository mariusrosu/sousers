package com.redcoding.sousers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.redcoding.sousers.ui.components.ErrorMessage
import com.redcoding.sousers.ui.components.InlineButtonState
import com.redcoding.sousers.ui.components.LoadingIndicator
import com.redcoding.sousers.ui.components.UserCard
import com.redcoding.sousers.ui.components.UserCardState
import com.redcoding.sousers.ui.theme.StackOverflowUsersTheme
import com.redcoding.sousers.ui.util.Lce
import com.redcoding.sousers.ui.util.asPlainString

@Composable
internal fun UserListScreen(viewModel: UsersListViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UserListScreen(uiState)
}

@Composable
private fun UserListScreen(uiState: Lce<UiState>) {
    StackOverflowUsersTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                when (uiState) {
                    is Lce.Loading -> LoadingIndicator()
                    is Lce.Content -> UserList(uiState.data)
                    is Lce.Error -> ErrorMessage(uiState.message)
                }
            }
        }
    }
}

@Composable
private fun UserList(uiState: UiState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = uiState.users,
            itemContent = { UserCard(it) },
        )
    }
}

@Preview
@Composable
private fun UserListScreenPreview() {
    val state = Lce.Content(
        data = UiState(
            users = listOf(
                UserCardState(
                    profilePictureUrl = "",
                    title = "First user".asPlainString(),
                    reputation = "Reputation: 1,000,000".asPlainString(),
                    buttonState = InlineButtonState("Follow".asPlainString()) {},
                ),
                UserCardState(
                    profilePictureUrl = "",
                    title = "Second user".asPlainString(),
                    reputation = "Reputation: 2,000,000".asPlainString(),
                    buttonState = InlineButtonState("Follow".asPlainString()) {},
                ),
                UserCardState(
                    profilePictureUrl = "",
                    title = "Second user".asPlainString(),
                    reputation = "Reputation: 3,000,000".asPlainString(),
                    buttonState = InlineButtonState("Follow".asPlainString()) {},
                ),
            )
        )
    )
    StackOverflowUsersTheme {
        UserListScreen(state)
    }
}
