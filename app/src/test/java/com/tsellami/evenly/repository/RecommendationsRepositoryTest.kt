package com.tsellami.evenly.repository

import com.tsellami.evenly.repository.rooms.VenuesDao
import com.tsellami.evenly.repository.rooms.VenuesRemoteKeyDao
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RecommendationsRepositoryTest : BaseRepositoryTest() {

    private lateinit var recommendationsRepository: RecommendationsRepository
    private val venuesDao: VenuesDao = mockk(relaxed = true)
    private val venuesRemoteKeyDao: VenuesRemoteKeyDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        recommendationsRepository = RecommendationsRepository(
            venuesDatabase, foursquareApi
        )
        every { venuesDatabase.venuesDao() } returns venuesDao
        every { venuesDatabase.remoteKeysDao() } returns venuesRemoteKeyDao
    }

    @Test
    fun `Assert that deleteAllRecommendations will clear remote key and all the venues`() {
        runBlocking {
            recommendationsRepository.deleteAllRecommendations()

            coVerify {
                venuesDao.deleteAllVenues()
                venuesRemoteKeyDao.deleteRemoteKey()
            }
        }
    }
}