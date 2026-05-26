package com.redcoding.sousers.ui.userdetails

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.redcoding.sousers.ui.components.ErrorMessage
import com.redcoding.sousers.ui.components.InlineButton
import com.redcoding.sousers.ui.components.InlineButtonState
import com.redcoding.sousers.ui.components.LoadingIndicator
import com.redcoding.sousers.ui.theme.StackOverflowUsersTheme
import com.redcoding.sousers.ui.util.Lce
import com.redcoding.sousers.ui.util.StringData
import com.redcoding.sousers.ui.util.getContentOrNull

@Composable
internal fun UserDetailsScreen(viewModel: UserDetailsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UserDetailsScreen(uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserDetailsScreen(uiState: Lce<UiState>) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    StackOverflowUsersTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        uiState.getContentOrNull()?.let {
                            Text(text = it.toolbarTitle.resolve(), fontWeight = FontWeight.Bold)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBackPressedDispatcher?.onBackPressed() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                when (uiState) {
                    is Lce.Loading -> LoadingIndicator()
                    is Lce.Content -> UserDetailsContent(uiState.data)
                    is Lce.Error -> ErrorMessage(uiState.message)
                }
            }
        }
    }
}

@Composable
private fun UserDetailsContent(uiState: UiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurfaceVariant),
            model = uiState.profilePictureUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "${uiState.name.resolve()}'s profile picture",
        )
        Text(
            text = uiState.name.resolve(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        BadgeRow(uiState.badgeCounts)
        DetailRow(text = uiState.reputation)
        uiState.location?.let {
            DetailRow(text = it)
        }
        uiState.websiteUrl?.let {
            DetailRow(text = it)
        }
        InlineButton(state = uiState.followButtonState)
    }
}

@Composable
private fun BadgeRow(badgeCounts: BadgeCountsUiState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = badgeCounts.gold.resolve(),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = badgeCounts.silver.resolve(),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = badgeCounts.bronze.resolve(),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun DetailRow(text: StringData) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text.resolve(),
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
