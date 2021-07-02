package com.tsellami.evenly.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tsellami.evenly.network.recommendations.FoursquareApi
import com.tsellami.evenly.network.recommendations.FoursquareApi.Companion.BASE_URL
import com.tsellami.evenly.rooms.VenuesDatabase
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
}