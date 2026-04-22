package com.redcoding.sousers.ui.util

sealed interface Lce<out T> {

    data object Loading : Lce<Nothing>

    data class Content<T>(val data: T) : Lce<T>

    data class Error(val message: StringData) : Lce<Nothing>
}

fun <T> Lce<T>.getContentOrNull(): T? = (this as? Lce.Content<T>)?.data
