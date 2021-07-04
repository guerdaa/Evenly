package com.tsellami.evenly.repository

import androidx.paging.PagingSource
import com.tsellami.evenly.repository.api.IFavoritesRepository
import com.tsellami.evenly.repository.network.FoursquareApi
import com.tsellami.evenly.repository.rooms.VenuesDatabase
import com.tsellami.evenly.repository.rooms.models.FavoriteVenue
import com.tsellami.evenly.repository.rooms.models.Venue
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val venuesDatabase: VenuesDatabase
) : IFavoritesRepository {

    override suspend fun retrieveIsFavorite(id: String): Boolean {
        return venuesDatabase.venuesDao().getFavorite(id) != null
    }

    override suspend fun addToFavorites(id: String) {
        venuesDatabase.venuesDao().addToFavorite(FavoriteVenue(id))
    }

    override suspend fun removeFromFavorites(id: String) {
        venuesDatabase.venuesDao().removeFromFavorites(id)
    }

    override fun retrieveFavorites(): PagingSource<Int, Venue> {
        return venuesDatabase.venuesDao().retrieveAllFavoriteVenues()
    }
}