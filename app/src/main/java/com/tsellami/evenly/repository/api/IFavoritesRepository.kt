package com.tsellami.evenly.repository.api

import androidx.paging.PagingSource
import com.tsellami.evenly.repository.rooms.models.Venue

interface IFavoritesRepository {

    suspend fun retrieveIsFavorite(id: String): Boolean

    suspend fun addToFavorites(id: String)

    suspend fun removeFromFavorites(id: String)

    fun retrieveFavorites(): PagingSource<Int, Venue>
}