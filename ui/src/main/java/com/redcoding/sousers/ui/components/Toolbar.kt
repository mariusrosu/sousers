package com.redcoding.sousers.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.redcoding.sousers.ui.theme.StackOverflowUsersTheme
import com.redcoding.sousers.ui.util.StringData
import com.redcoding.sousers.ui.util.asPlainString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Toolbar(text: StringData) {
    TopAppBar(
        title = {
            Text(
                text = text.resolve(),
                fontWeight = FontWeight.Bold,
            )
        }
    )
}

@Preview
@Composable
private fun ToolbarPreview() {
    StackOverflowUsersTheme {
        Toolbar(text = "Top Stack Overflow Users".asPlainString())
    }
}
