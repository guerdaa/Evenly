package com.tsellami.evenly.repository.rooms

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tsellami.evenly.repository.rooms.models.DetailedVenue
import com.tsellami.evenly.repository.rooms.models.FavoriteVenue
import com.tsellami.evenly.repository.rooms.models.Venue
import com.tsellami.evenly.repository.rooms.models.VenuesRemoteKey

@Database(
    entities = [Venue::class, VenuesRemoteKey::class, DetailedVenue::class, FavoriteVenue::class],
    version = 4
)
abstract class VenuesDatabase : RoomDatabase() {
    abstract fun venuesDao(): VenuesDao
    abstract fun remoteKeysDao(): VenuesRemoteKeyDao
}