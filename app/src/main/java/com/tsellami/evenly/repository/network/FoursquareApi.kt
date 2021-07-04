package com.tsellami.evenly.repository.network

import com.tsellami.evenly.BuildConfig
import com.tsellami.evenly.repository.network.dto.DetailedVenueResponse
import com.tsellami.evenly.repository.network.dto.RecommendationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareApi {

    @GET("explore")
    suspend fun getRecommendations(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
        @Query("v") v: String = BuildConfig.V,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int,
        @Query("ll") location: String = BuildConfig.LOCATION
    ): RecommendationResponse

    @GET("{id}")
    suspend fun getDetailedVenue(
        @Path("id") id: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
        @Query("v") v: String = BuildConfig.V,
    ): DetailedVenueResponse

    companion object {
        const val BASE_URL = "https://api.foursquare.com/v2/venues/"
    }
}