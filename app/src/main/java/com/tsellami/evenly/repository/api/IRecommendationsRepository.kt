package com.tsellami.evenly.repository.api

import androidx.paging.PagingData
import com.tsellami.evenly.repository.rooms.models.Venue
import kotlinx.coroutines.flow.Flow

interface IRecommendationsRepository {

    fun getRecommendationsResultPaged(): Flow<PagingData<Venue>>

    suspend fun deleteAllRecommendations()
}