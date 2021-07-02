package com.tsellami.evenly.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tsellami.evenly.network.recommendations.FoursquareApi
import com.tsellami.evenly.rooms.Venue
import com.tsellami.evenly.rooms.VenuesDatabase
import com.tsellami.evenly.rooms.VenuesRemoteKey
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class VenuesRemoteMediator(
    private val database: VenuesDatabase,
    private val foursquareApi: FoursquareApi
) : RemoteMediator<Int, Venue>(){

    private val remoteKeyDao = database.remoteKeysDao()
    private val venuesDao = database.venuesDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Venue>): MediatorResult {
        try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    if (remoteKeyDao.getRemoteKey() == null)
                        0
                    else
                        remoteKeyDao.getRemoteKey()!!.nextPageKey
                }
            }

            val data = foursquareApi.getRecommendations(offset = loadKey.times(50))
            val venues = data.response.groups.first().items.map {
                it.venue.let { venue ->
                    Venue(venue.id, venue.name, venue.categories.first().name, venue.formattedLocation, venue.location.distance, false)
                }
            }
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    venuesDao.deleteAllVenues()
                    remoteKeyDao.deleteRemoteKey()
                }
                val nextLoadKey = loadKey + 1
                venuesDao.insertAll(venues)
                remoteKeyDao.insertRemoteKey(
                    VenuesRemoteKey(nextPageKey = nextLoadKey)
                )
            }
            return MediatorResult.Success(endOfPaginationReached = venues.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }
}