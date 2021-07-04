package com.tsellami.evenly.ui.detailedVenue

import com.tsellami.evenly.repository.api.IDetailedVenueRepository
import com.tsellami.evenly.repository.api.IFavoritesRepository
import com.tsellami.evenly.repository.rooms.models.DetailedVenue
import com.tsellami.evenly.ui.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DetailedVenueViewModelTest : BaseViewModelTest() {

    private val detailedVenueRepository: IDetailedVenueRepository = mockk(relaxed = true)
    private val favoritesRepository: IFavoritesRepository = mockk(relaxed = true)
    private lateinit var detailedVenueViewModel: DetailedVenueViewModel

    @Before
    fun setUp() {
        detailedVenueViewModel = DetailedVenueViewModel(
            detailedVenueRepository,
            favoritesRepository
        )
    }

    @Test
    fun `Assert that Loading is sent direct after initialization`() {
        runBlocking {
            assertTrue(
                detailedVenueViewModel.resource.first()
                        is DetailedVenueViewModel.RetrievingEvent.Loading
            )
        }
    }

    @Test
    fun `Assert that retrieveDetails will send Success after finishing`() {
        runBlocking {
            val detailedVenue: DetailedVenue = mockk(relaxed = true)
            coEvery { detailedVenueRepository.retrieveVenueDetails(ID) } returns detailedVenue
            coEvery { favoritesRepository.retrieveIsFavorite(ID) } returns true
            detailedVenueViewModel.resource.first()

            detailedVenueViewModel.retrieveDetails(ID)

            assertEquals(detailedVenueViewModel.isFavorite.value, true)
            assertEquals(detailedVenueViewModel.details.value, detailedVenue)
            assertTrue(
                detailedVenueViewModel.resource.first()
                        is DetailedVenueViewModel.RetrievingEvent.Success
            )
        }
    }

    @Test
    fun `Assert that clicking on favorite will insert a venue to favorites if it is unmarked`() {
        runBlocking {
            coEvery { favoritesRepository.retrieveIsFavorite(ID) } returns false

            detailedVenueViewModel.retrieveDetails(ID)
            detailedVenueViewModel.onFavoriteClickListener(ID)

            assertTrue(detailedVenueViewModel.isFavorite.value!!)
            coVerify { favoritesRepository.addToFavorites(ID) }
            coVerify(exactly = 0) { favoritesRepository.removeFromFavorites(ID) }
        }
    }

    @Test
    fun `Assert that clicking on favorite will remove a venue from favorites if it is already marked as favorite`() {
        runBlocking {
            coEvery { favoritesRepository.retrieveIsFavorite(ID) } returns true

            detailedVenueViewModel.retrieveDetails(ID)
            detailedVenueViewModel.onFavoriteClickListener(ID)

            assertFalse(detailedVenueViewModel.isFavorite.value!!)
            coVerify { favoritesRepository.removeFromFavorites(ID) }
            coVerify(exactly = 0) { favoritesRepository.addToFavorites(ID) }
        }
    }

    companion object {
        private const val ID = "ID"
    }
}