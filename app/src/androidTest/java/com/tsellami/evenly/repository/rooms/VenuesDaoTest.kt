package com.tsellami.evenly.repository.rooms

import androidx.paging.PagingSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.tsellami.evenly.repository.rooms.models.DetailedVenue
import com.tsellami.evenly.repository.rooms.models.FavoriteVenue
import com.tsellami.evenly.repository.rooms.models.Venue
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class VenuesDaoTest: DaoTest(){

    private lateinit var dao: VenuesDao
    private val venue1 = Venue("ID1", "NAME1", "CATEGORY1", "ADDRESS1", 1)
    private val venue2 = Venue("ID2", "NAME2", "CATEGORY2", "ADDRESS2", 2)

    @Before
    override fun setUp() {
        super.setUp()
        dao = venuesDatabase.venuesDao()
    }

    @Test
    fun testGetAllVenues() {
        runBlocking {
            dao.insertAll(listOf(venue1, venue2))

            val result = dao.getAllVenues()

            Assert.assertEquals(
                PagingSource.LoadResult.Page(
                    data = listOf(venue1, venue2),
                    prevKey = null,
                    nextKey = null,
                    itemsAfter = 0,
                    itemsBefore = 0
                ),
                result.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 2,
                        placeholdersEnabled = false
                    )
                )
            )
        }
    }

    @Test
    fun testGetVenue_afterInsertAll() {
        runBlocking {
            dao.insertAll(listOf(venue1, venue2))

            val retrievedVenue1 = dao.getVenue("ID1")
            val retrievedVenue2 = dao.getVenue("ID2")

            Assert.assertEquals(retrievedVenue1, venue1)
            Assert.assertEquals(retrievedVenue2, venue2)
        }
    }

    @Test
    fun testInsertAll_OnConflict() {
        runBlocking {
            val venue3 = Venue("ID1", "NAME2", "CATEGORY2", "ADDRESS2", 2)
            dao.insertAll(listOf(venue1, venue3))

            val retrievedVenue1 = dao.getVenue("ID1")

            Assert.assertNotEquals(retrievedVenue1, venue1)
            Assert.assertEquals(retrievedVenue1, venue3)
        }
    }

    @Test
    fun testDeleteAllVenues() {
        runBlocking {
            dao.insertAll(listOf(venue1, venue2))

            dao.deleteAllVenues()
            val retrievedVenue1 = dao.getVenue("ID1")
            val retrievedVenue2 = dao.getVenue("ID2")

            Assert.assertNull(retrievedVenue1)
            Assert.assertNull(retrievedVenue2)
        }
    }

    @Test
    fun testUpdateVenue() {
        runBlocking {
            val updatedVenue = venue1.copy(name = "Modified Name")
            dao.insertAll(listOf(venue1))

            dao.updateVenue(updatedVenue)
            val result = dao.getVenue(venue1.id)

            Assert.assertEquals(updatedVenue, result)
        }
    }

    @Test
    fun testInsertVenueDetails() {
        runBlocking {
            val detailedVenue = DetailedVenue(
                "ID",
                "PRICE",
                1f,
                "OPENING HOURS",
                "URL",
                "THUMBNAIL",
                1f,
                1f
            )

            dao.insertVenueDetails(detailedVenue)
            val result = dao.getVenueDetails("ID")

            Assert.assertEquals(detailedVenue, result)
        }
    }

    @Test
    fun testInsertVenueDetails_OnConflict() {
        runBlocking {
            val detailedVenue = DetailedVenue(
                "ID",
                "PRICE",
                1f,
                "OPENING HOURS",
                "URL",
                "THUMBNAIL",
                1f,
                1f
            )
            val newEntry = detailedVenue.copy(price = "NEW_PRICE")

            dao.insertVenueDetails(detailedVenue)
            dao.insertVenueDetails(newEntry)
            val result = dao.getVenueDetails("ID")

            Assert.assertEquals(newEntry, result)
            Assert.assertNotEquals(detailedVenue, result)
        }
    }

    @Test
    fun testAddToFavorite() {
        runBlocking {
            val favorite = FavoriteVenue("ID")

            dao.addToFavorite(favorite)
            val result = dao.getFavorite("ID")

            Assert.assertEquals(favorite, result)
        }
    }

    @Test
    fun testRemoveFromFavorites() {
        runBlocking {
            val favorite = FavoriteVenue("ID")
            dao.addToFavorite(favorite)

            dao.removeFromFavorites("ID")
            val result = dao.getFavorite("ID")

            Assert.assertNull(result)
        }
    }

    @Test
    fun testRetrieveAllFavoriteVenues() {
        runBlocking {
            val favorite1 = FavoriteVenue("ID1")
            dao.insertAll(listOf(venue1, venue2))
            dao.addToFavorite(favorite1)

            val result = dao.retrieveAllFavoriteVenues()

            Assert.assertEquals(
                PagingSource.LoadResult.Page(
                    data = listOf(venue1),
                    prevKey = null,
                    nextKey = null,
                    itemsAfter = 0,
                    itemsBefore = 0
                ),
                result.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 2,
                        placeholdersEnabled = false
                    )
                )
            )
        }
    }
}