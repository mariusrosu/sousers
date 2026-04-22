package com.redcoding.sousers.ui.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.redcoding.sousers.ui.theme.StackOverflowUsersTheme
import com.redcoding.sousers.ui.util.StringData
import com.redcoding.sousers.ui.util.asPlainString

@Composable
internal fun InlineButton(state: InlineButtonState) {
    Button(
        modifier = Modifier.wrapContentSize(),
        onClick = state.onClick,
    ) {
        Text(state.text.resolve())
    }
}

@Immutable
internal data class InlineButtonState(
    val text: StringData,
    val onClick: () -> Unit,
)

@Preview
@Composable
private fun InlineButtonPreview() {
    val state = InlineButtonState(
        text = "Follow".asPlainString(),
    ) {}
    StackOverflowUsersTheme {
        InlineButton(state)
    }
}
