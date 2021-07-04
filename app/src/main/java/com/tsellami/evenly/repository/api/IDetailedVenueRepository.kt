package com.tsellami.evenly.repository.api

import com.tsellami.evenly.repository.rooms.models.DetailedVenue

interface IDetailedVenueRepository {

    suspend fun retrieveVenueDetails(id: String): DetailedVenue?
}