package com.tsellami.evenly.repository.rooms

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
open class DaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var venuesDatabase: VenuesDatabase

    @Before
    open fun setUp() {
        venuesDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VenuesDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        venuesDatabase.close()
    }
}