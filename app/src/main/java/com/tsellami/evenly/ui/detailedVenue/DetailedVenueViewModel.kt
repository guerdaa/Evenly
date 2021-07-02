package com.tsellami.evenly.ui.detailedVenue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tsellami.evenly.repository.VenuesRepository
import com.tsellami.evenly.rooms.DetailedVenue
import com.tsellami.evenly.ui.LoadingViewModel
import com.tsellami.evenly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedVenueViewModel @Inject constructor(
    private val repository: VenuesRepository
) : LoadingViewModel() {

    private val _details = MutableLiveData<DetailedVenue>()
    val details : LiveData<DetailedVenue>
        get() = _details

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    fun retrieveDetails(id: String) {
        viewModelScope.launch {
            _details.value = repository.retrieveVenueDetails(id)
            _isFavorite.value = repository.retrieveIsFavorite(id)
            channel.send(Resource.Success)
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
                    repository.addToFavorites(id)
                } else {
                    repository.removeFromFavorites(id)
                }
            }
        }
    }
}