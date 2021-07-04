package com.tsellami.evenly.ui.detailedVenue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsellami.evenly.repository.api.IDetailedVenueRepository
import com.tsellami.evenly.repository.api.IFavoritesRepository
import com.tsellami.evenly.repository.rooms.models.DetailedVenue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedVenueViewModel @Inject constructor(
    private val detailedVenueRepository: IDetailedVenueRepository,
    private val favoritesRepository: IFavoritesRepository
) : ViewModel() {

    private val channel = Channel<RetrievingEvent>()
    val resource = channel.receiveAsFlow()

    private val _details = MutableLiveData<DetailedVenue>()
    val details: LiveData<DetailedVenue>
        get() = _details

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    init {
        viewModelScope.launch {
            channel.send(RetrievingEvent.Loading)
        }
    }

    fun retrieveDetails(id: String) {
        viewModelScope.launch {
            _details.value = detailedVenueRepository.retrieveVenueDetails(id)
            _isFavorite.value = favoritesRepository.retrieveIsFavorite(id)
            channel.send(RetrievingEvent.Success)
        }
    }

    fun onFavoriteClickListener(id: String) {
        _isFavorite.value = _isFavorite.value!!.not()
        updateFavorites(id)
    }

    private fun updateFavorites(id: String) {
        viewModelScope.launch {
            _isFavorite.value?.let { favorite ->
                if (favorite) {
                    favoritesRepository.addToFavorites(id)
                } else {
                    favoritesRepository.removeFromFavorites(id)
                }
            }
        }
    }

    sealed class RetrievingEvent {
        object Loading : RetrievingEvent()
        object Success : RetrievingEvent()
    }
}