package com.tsellami.evenly.repository

import com.tsellami.evenly.repository.rooms.VenuesDao
import com.tsellami.evenly.repository.rooms.models.DetailedVenue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class DetailedVenueRepositoryTest : BaseRepositoryTest() {

    private val venuesDao: VenuesDao = mockk(relaxed = true)

    private lateinit var detailedVenueRepository: DetailedVenueRepository

    @Before
    fun setUp() {
        detailedVenueRepository = DetailedVenueRepository(
            venuesDatabase = venuesDatabase,
            foursquareApi = foursquareApi
        )
        every { venuesDatabase.venuesDao() } returns venuesDao
    }

    @Test
    fun `Assert that details are retrieved remotely if they are not cached`() {
        runBlocking {
            val detailedVenue = DetailedVenue(ID, null, 0f, null, "", "", 0f, 0f)
            coEvery { venuesDao.getVenueDetails(ID) } returns null
            coEvery { foursquareApi.getDetailedVenue(ID).toEntity() } returns detailedVenue

            val result = detailedVenueRepository.retrieveVenueDetails(ID)

            coVerify { venuesDao.insertVenueDetails(detailedVenue) }
            assertEquals(result, detailedVenue)
        }
    }

    @Test
    fun `Assert that details are retrieved locally if they are already cached`() {
        runBlocking {
            val detailedVenue = DetailedVenue(ID, null, 0f, null, "", "", 0f, 0f)
            coEvery { venuesDao.getVenueDetails(ID) } returns detailedVenue

            val result = detailedVenueRepository.retrieveVenueDetails(ID)

            coVerify(exactly = 0) { foursquareApi.getDetailedVenue(any()) }
            coVerify(exactly = 0) { venuesDao.insertVenueDetails(detailedVenue) }
            assertEquals(result, detailedVenue)
        }
    }

    @Test
    fun `Assert that details are null after retrieving remotely failed`() {
        runBlocking {
            coEvery { venuesDao.getVenueDetails(ID) } returns null
            coEvery { foursquareApi.getDetailedVenue(ID).toEntity() } throws Exception()

            val result = detailedVenueRepository.retrieveVenueDetails(ID)

            assertNull(result)
        }
    }
}