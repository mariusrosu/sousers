package com.redcoding.sousers.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.redcoding.sousers.ui.theme.StackOverflowUsersTheme
import com.redcoding.sousers.ui.util.StringData
import com.redcoding.sousers.ui.util.asPlainString

@Composable
internal fun ErrorMessage(message: StringData) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp),
        textAlign = TextAlign.Center,
        text = message.resolve(),
        color = MaterialTheme.colorScheme.error,
    )
}

@Preview
@Composable
private fun ErrorMessagePreview() {
    StackOverflowUsersTheme {
        ErrorMessage(message = "Unknown error!".asPlainString())
    }
}
