package com.redcoding.sousers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.redcoding.sousers.business.model.User
import com.redcoding.sousers.ui.theme.StackOverflowUsersTheme
import com.redcoding.sousers.ui.util.Lce

@Composable
internal fun UserListScreen(viewModel: UsersListViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StackOverflowUsersTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (val state = uiState) {
                    is Lce.Loading -> LoadingScreen()
                    is Lce.Content -> UserListScreen(state.data)
                    is Lce.Error -> ErrorScreen(state.throwable)
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    TODO("Will be implemented in a separate commit")
}

@Composable
private fun UserListScreen(uiState: UiState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = uiState.users,
            key = { it.id },
            itemContent = { UserCard(it) },
        )
    }
}

@Composable
private fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = user.name,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = user.reputation.toString(),
        )
    }
}

@Composable
private fun ErrorScreen(throwable: Throwable) {
    TODO("Will be implemented in a separate commit")
}
