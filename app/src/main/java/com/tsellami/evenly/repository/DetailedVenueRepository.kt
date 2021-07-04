package com.tsellami.evenly.repository

import com.tsellami.evenly.repository.api.IDetailedVenueRepository
import com.tsellami.evenly.repository.network.FoursquareApi
import com.tsellami.evenly.repository.rooms.VenuesDatabase
import com.tsellami.evenly.repository.rooms.models.DetailedVenue
import javax.inject.Inject

class DetailedVenueRepository @Inject constructor(
    private val venuesDatabase: VenuesDatabase,
    private val foursquareApi: FoursquareApi
) : IDetailedVenueRepository {

    override suspend fun retrieveVenueDetails(id: String): DetailedVenue? {
        return venuesDatabase.venuesDao().getVenueDetails(id)
            ?: return try {
                val details = foursquareApi.getDetailedVenue(id).toEntity()
                venuesDatabase.venuesDao().insertVenueDetails(details)
                details
            } catch (e: Exception) {
                null
            }
    }
}