package com.tsellami.evenly.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.tsellami.evenly.repository.api.IFavoritesRepository
import com.tsellami.evenly.repository.rooms.models.Venue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: IFavoritesRepository
) : ViewModel() {

    val favorites: LiveData<PagingData<Venue>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = false
        )
    ) {
        favoritesRepository.retrieveFavorites()
    }.liveData.cachedIn(viewModelScope)
}