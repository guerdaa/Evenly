package com.tsellami.evenly.di

import android.app.Application
import androidx.room.Room
import com.tsellami.evenly.repository.DetailedVenueRepository
import com.tsellami.evenly.repository.FavoritesRepository
import com.tsellami.evenly.repository.RecommendationsRepository
import com.tsellami.evenly.repository.api.IDetailedVenueRepository
import com.tsellami.evenly.repository.api.IFavoritesRepository
import com.tsellami.evenly.repository.api.IRecommendationsRepository
import com.tsellami.evenly.repository.network.FoursquareApi
import com.tsellami.evenly.repository.network.FoursquareApi.Companion.BASE_URL
import com.tsellami.evenly.repository.rooms.VenuesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideFoursquareApi(): FoursquareApi =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(FoursquareApi::class.java)

    @Provides
    @Singleton
    fun provideVenuesDatabase(app: Application): VenuesDatabase =
        Room.databaseBuilder(app, VenuesDatabase::class.java, "venues_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideRecommendationsRepository(
        venuesDatabase: VenuesDatabase,
        foursquareApi: FoursquareApi
    ): IRecommendationsRepository = RecommendationsRepository(venuesDatabase, foursquareApi)

    @Provides
    @Singleton
    fun provideDetailedVenueRepository(
        venuesDatabase: VenuesDatabase,
        foursquareApi: FoursquareApi
    ): IDetailedVenueRepository = DetailedVenueRepository(venuesDatabase, foursquareApi)

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        venuesDatabase: VenuesDatabase,
    ): IFavoritesRepository = FavoritesRepository(venuesDatabase)
}