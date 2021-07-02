package com.tsellami.evenly.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.tsellami.evenly.network.recommendations.FoursquareApi
import com.tsellami.evenly.rooms.DetailedVenue
import com.tsellami.evenly.rooms.FavoriteVenue
import com.tsellami.evenly.rooms.Venue
import com.tsellami.evenly.rooms.VenuesDatabase
import javax.inject.Inject

class VenuesRepository @Inject constructor(
    private val venuesDatabase: VenuesDatabase,
    private val foursquareApi: FoursquareApi
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getRecommendationsResultPaged() =
        Pager(
            config = PagingConfig(pageSize = 50, maxSize = 200),
            remoteMediator = VenuesRemoteMediator(venuesDatabase, foursquareApi),
            pagingSourceFactory = { venuesDatabase.venuesDao().getAllVenues() }
        ).flow

    suspend fun deleteAllRecommendations() {
        venuesDatabase.venuesDao().deleteAllVenues()
        venuesDatabase.remoteKeysDao().deleteRemoteKey()
    }

    suspend fun retrieveVenueDetails(id: String): DetailedVenue {
        val retrievedEntity = venuesDatabase.venuesDao().getVenueDetails(id)
        if (retrievedEntity == null) {
            val details = foursquareApi.getDetailedVenue(id).toEntity()
            venuesDatabase.venuesDao().insertVenueDetails(details)
            return details
        }
        return retrievedEntity
    }

    suspend fun retrieveIsFavorite(id: String): Boolean {
        return venuesDatabase.venuesDao().getFavorite(id) != null
    }


    suspend fun addToFavorites(id: String) {
        venuesDatabase.venuesDao().addToFavorite(FavoriteVenue(id))
    }

    suspend fun removeFromFavorites(id: String) {
        venuesDatabase.venuesDao().removeFromFavorites(id)
    }

    fun retrieveFavorites(): PagingSource<Int, Venue> {
        return venuesDatabase.venuesDao().retrieveAllFavoriteVenues()
    }
}