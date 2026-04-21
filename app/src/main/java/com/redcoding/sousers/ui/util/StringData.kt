package com.redcoding.sousers.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class StringData {

    data class PlainString(val value: String) : StringData()

    class ResourceString(
        @param:StringRes val id: Int,
        vararg val args: Any,
    ) : StringData()

    @Composable
    fun resolve(): String = when (this) {
        is PlainString -> value
        is ResourceString -> stringResource(id, *args)
    }
}

fun Int.asResourceString(vararg args: Any) = StringData.ResourceString(this, *args)

fun String.asPlainString() = StringData.PlainString(this)
