package com.tsellami.evenly.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.tsellami.evenly.repository.api.IRecommendationsRepository
import com.tsellami.evenly.repository.network.FoursquareApi
import com.tsellami.evenly.repository.rooms.VenuesDatabase
import javax.inject.Inject

class RecommendationsRepository @Inject constructor(
    private val venuesDatabase: VenuesDatabase,
    private val foursquareApi: FoursquareApi
) : IRecommendationsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getRecommendationsResultPaged() =
        Pager(
            config = PagingConfig(
                pageSize = 50,
                maxSize = 200,
                enablePlaceholders = false
            ),
            remoteMediator = VenuesRemoteMediator(venuesDatabase, foursquareApi),
            pagingSourceFactory = { venuesDatabase.venuesDao().getAllVenues() }
        ).flow

    override suspend fun deleteAllRecommendations() {
        venuesDatabase.venuesDao().deleteAllVenues()
        venuesDatabase.remoteKeysDao().deleteRemoteKey()
    }
}