package com.redcoding.sousers.ui.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

internal class LceTest {

    @Test
    fun `Should return data when Lce is of type content`() {
        val lce = Lce.Content(FakeUiState)

        val result = lce.getContentOrNull()

        assertEquals(result, FakeUiState)
    }

    @Test
    fun `Should return null when Lce is of type loading`() {
        val lce = Lce.Loading

        val result = lce.getContentOrNull()

        assertNull(result)
    }

    @Test
    fun `Should return null when Lce is of type error`() {
        val lce = Lce.Error(message = StringData.PlainString("No connection!"))

        val result = lce.getContentOrNull()

        assertNull(result)
    }
}

private data object FakeUiState
