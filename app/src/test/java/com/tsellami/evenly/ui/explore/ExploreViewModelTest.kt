package com.tsellami.evenly.ui.explore

import com.tsellami.evenly.repository.api.IRecommendationsRepository
import com.tsellami.evenly.ui.BaseViewModelTest
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ExploreViewModelTest : BaseViewModelTest() {

    private val recommendationsRepository: IRecommendationsRepository = mockk(relaxed = true)
    private lateinit var exploreViewModel: ExploreViewModel

    @Before
    fun setUp() {
        exploreViewModel = ExploreViewModel(
            recommendationsRepository
        )
    }

    @Test
    fun `Assert that deleteCachedData will clear all the recommendations`() {
        runBlocking {
            exploreViewModel.deleteCachedData()

            coVerify { recommendationsRepository.deleteAllRecommendations() }
        }
    }
}