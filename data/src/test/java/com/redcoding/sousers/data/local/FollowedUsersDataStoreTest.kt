package com.redcoding.sousers.data.local

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FollowedUsersDataStoreTest {

    private val keyValueStorage: KeyValueStorage = mockk()

    private lateinit var dataStore: FollowedUsersDataStore

    @Test
    fun `Should emit all followed users IDs when there are users stored`() {
        every { keyValueStorage.getStringSet(any()) } returns setOf("1", "2", "3")

        dataStore = createDataStore()

        assertEquals(
            dataStore.followedUserIds.value,
            setOf(1L, 2L, 3L),
        )
    }

    @Test
    fun `Should emit empty followed users IDs when there are no users stored`() {
        every { keyValueStorage.getStringSet(any()) } returns emptySet()

        dataStore = createDataStore()

        assertEquals(
            dataStore.followedUserIds.value,
            emptySet<Long>(),
        )
    }

    @Test
    fun `Should update storage with new user ID when user is followed`() {
        every { keyValueStorage.getStringSet(any()) } returns setOf("1", "2", "3")
        every { keyValueStorage.putStringSet(any(), any()) } just Runs

        dataStore = createDataStore()
        dataStore.follow(4L)

        verify { keyValueStorage.putStringSet(any(), setOf("1", "2", "3", "4")) }
        assertEquals(
            dataStore.followedUserIds.value,
            setOf(1L, 2L, 3L, 4L),
        )
    }

    @Test
    fun `Should update storage without user ID when user is unfollowed`() {
        every { keyValueStorage.getStringSet(any()) } returns setOf("1", "2", "3")
        every { keyValueStorage.putStringSet(any(), any()) } just Runs

        dataStore = createDataStore()
        dataStore.unfollow(2L)

        verify { keyValueStorage.putStringSet(any(), setOf("1", "3")) }
        assertEquals(
            dataStore.followedUserIds.value,
            setOf(1L, 3L),
        )
    }

    private fun createDataStore() = FollowedUsersDataStore(keyValueStorage)
}
