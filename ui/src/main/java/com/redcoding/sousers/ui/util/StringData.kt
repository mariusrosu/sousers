package com.redcoding.sousers.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class StringData {

    data class PlainString(val value: String) : StringData()

    data class ResourceString(
        @param:StringRes val id: Int,
        val args: List<Any> = emptyList(),
    ) : StringData() {

        constructor(@StringRes id: Int, vararg args: Any) : this(id, args.toList())
    }

    @Composable
    fun resolve(): String = when (this) {
        is PlainString -> value
        is ResourceString -> stringResource(id, *args.toTypedArray())
    }
}

fun Int.asResourceString(vararg args: Any) = StringData.ResourceString(this, *args)

fun String.asPlainString() = StringData.PlainString(this)
