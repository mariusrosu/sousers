package com.redcoding.sousers.ui.util

sealed interface Lce<out T> {
    data object Loading : Lce<Nothing>
    data class Content<T>(val data: T) : Lce<T>
    data class Error(val throwable: Throwable) : Lce<Nothing>
}
