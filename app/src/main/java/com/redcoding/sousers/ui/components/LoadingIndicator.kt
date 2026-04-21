package com.redcoding.sousers.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.redcoding.sousers.ui.theme.StackOverflowUsersTheme

@Composable
internal fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(64.dp),
    )
}

@Preview
@Composable
private fun LoadingIndicatorPreview() {
    StackOverflowUsersTheme {
        LoadingIndicator()
    }
}
