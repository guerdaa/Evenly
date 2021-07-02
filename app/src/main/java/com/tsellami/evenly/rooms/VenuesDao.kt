package com.tsellami.evenly.rooms

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface VenuesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(venues: List<Venue>)

    @Query("SELECT * FROM venues")
    fun getAllVenues(): PagingSource<Int, Venue>

    @Query("SELECT * FROM venues WHERE id = :id")
    suspend fun getVenue(id: String): Venue

    @Update
    suspend fun updateFavorite(venue: Venue)

    @Query("DELETE FROM venues")
    suspend fun deleteAllVenues()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenueDetails(detailedVenue: DetailedVenue)

    @Query("SELECT * FROM detailed_venues WHERE id = :id")
    suspend fun getVenueDetails(id: String): DetailedVenue?

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getFavorite(id: String): FavoriteVenue?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(favoriteVenue: FavoriteVenue)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun removeFromFavorites(id: String)

    @Query("SELECT * FROM favorites INNER JOIN venues ON favorites.id = venues.id")
    fun retrieveAllFavoriteVenues(): PagingSource<Int, Venue>
}