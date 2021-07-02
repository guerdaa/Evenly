package com.tsellami.evenly.rooms

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venues_remote_keys")
data class VenuesRemoteKey(
    @PrimaryKey val location: String = "evenly",
    val nextPageKey: Int
)