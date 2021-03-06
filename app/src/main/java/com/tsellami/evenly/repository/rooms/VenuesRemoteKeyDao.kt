package com.tsellami.evenly.repository.rooms

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tsellami.evenly.repository.rooms.models.VenuesRemoteKey

@Dao
interface VenuesRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(venuesRemoteKey: VenuesRemoteKey)

    @Query("SELECT * FROM venues_remote_keys WHERE location = :location")
    suspend fun getRemoteKey(location: String = "evenly"): VenuesRemoteKey?

    @Query("DELETE FROM venues_remote_keys WHERE location = :location")
    suspend fun deleteRemoteKey(location: String = "evenly")
}