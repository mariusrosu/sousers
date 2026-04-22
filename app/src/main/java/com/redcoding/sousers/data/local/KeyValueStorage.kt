package com.redcoding.sousers.data.local

internal interface KeyValueStorage {

    fun getStringSet(key: String): Set<String>

    fun putStringSet(key: String, values: Set<String>)
}
