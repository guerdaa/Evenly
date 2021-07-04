package com.tsellami.evenly.repository

import androidx.paging.PagingSource
import com.tsellami.evenly.repository.rooms.VenuesDao
import com.tsellami.evenly.repository.rooms.models.FavoriteVenue
import com.tsellami.evenly.repository.rooms.models.Venue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavoritesRepositoryTest : BaseRepositoryTest() {

    private lateinit var favoritesRepository: FavoritesRepository
    private val venuesDao: VenuesDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        favoritesRepository = FavoritesRepository(venuesDatabase)
        every { venuesDatabase.venuesDao() } returns venuesDao
    }

    @Test
    fun `Assert that retrieveIsFavorite return true if an entry ID is stored in database`() {
        runBlocking {
            coEvery { venuesDao.getFavorite(ID) } returns FavoriteVenue(ID)

            val result = favoritesRepository.retrieveIsFavorite(ID)

            assertTrue(result)
        }
    }

    @Test
    fun `Assert that retrieveIsFavorite return false if there is no matches`() {
        runBlocking {
            coEvery { venuesDao.getFavorite(ID) } returns null

            val result = favoritesRepository.retrieveIsFavorite(ID)

            assertFalse(result)
        }
    }

    @Test
    fun `Assert addToFavorites adds a favorite to Room`() {
        runBlocking {
            favoritesRepository.addToFavorites(ID)

            coVerify { venuesDao.addToFavorite(FavoriteVenue(ID)) }
        }
    }

    @Test
    fun `Assert removeFromFavorites removes a favorite from Room`() {
        runBlocking {
            favoritesRepository.removeFromFavorites(ID)

            coVerify { venuesDao.removeFromFavorites(ID) }
        }
    }

    @Test
    fun `Assert retrieveFavorites returns a PagingSource`() {
        runBlocking {
            val pagingSource: PagingSource<Int, Venue> = mockk(relaxed = true)
            coEvery { venuesDao.retrieveAllFavoriteVenues() } returns pagingSource

            val result = favoritesRepository.retrieveFavorites()

            coVerify { venuesDao.retrieveAllFavoriteVenues() }
            assertEquals(pagingSource, result)
        }
    }
}