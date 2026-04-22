package com.redcoding.sousers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.redcoding.sousers.ui.theme.StackOverflowUsersTheme
import com.redcoding.sousers.ui.util.StringData
import com.redcoding.sousers.ui.util.asPlainString

@Composable
internal fun UserCard(state: UserCardState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ProfilePicture(
                profilePictureUrl = state.profilePictureUrl,
                userName = state.title.resolve(),
            )
            Column(
                modifier = Modifier.weight(1f),
            ) {
                TitleText(text = state.title.resolve())
                ReputationText(text = state.reputation.resolve())
            }
            InlineButton(state = state.buttonState)
        }
    }
}

@Composable
private fun ProfilePicture(profilePictureUrl: String, userName: String) {
    AsyncImage(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        model = profilePictureUrl,
        contentScale = ContentScale.Crop,
        contentDescription = "${userName}'s profile picture",
    )
}

@Composable
private fun TitleText(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        text = text,
    )
}

@Composable
private fun ReputationText(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 16.sp,
        text = text,
    )
}

@Immutable
internal data class UserCardState(
    val profilePictureUrl: String,
    val title: StringData,
    val reputation: StringData,
    val buttonState: InlineButtonState,
)

@Preview
@Composable
private fun UserCardPreview() {
    val state = UserCardState(
        profilePictureUrl = "https://google.com",
        title = "User Card Title".asPlainString(),
        reputation = "Reputation: 1,000,000".asPlainString(),
        buttonState = InlineButtonState(text = "Follow".asPlainString()) {},
    )
    StackOverflowUsersTheme {
        UserCard(state)
    }
}
