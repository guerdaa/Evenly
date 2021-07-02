package com.tsellami.evenly.rooms

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Venue::class, VenuesRemoteKey::class, DetailedVenue::class, FavoriteVenue::class],
    version = 3
)
abstract class VenuesDatabase: RoomDatabase() {
    abstract fun venuesDao(): VenuesDao
    abstract fun remoteKeysDao(): VenuesRemoteKeyDao
}