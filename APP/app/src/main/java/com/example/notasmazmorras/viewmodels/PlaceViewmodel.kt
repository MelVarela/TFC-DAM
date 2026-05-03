package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalPlace
import com.example.notasmazmorras.data.repositories.PlaceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaceViewmodel(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    val places : StateFlow<List<LocalPlace>> = placeRepository.getAllPlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertPlace(place: LocalPlace) = viewModelScope.launch {
        placeRepository.insertPlace(place)
    }

    fun updatePlace(place: LocalPlace) = viewModelScope.launch {
        placeRepository.updatePlace(place)
    }

    fun deletePlace(place: LocalPlace) = viewModelScope.launch {
        placeRepository.deletePlace(place)
    }

    fun sync(currentCampaign: String) = viewModelScope.launch {
        placeRepository.uploadPendingChanges()
        placeRepository.syncFromServer(currentCampaign)
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val placeRepository = application.container.placeRepository
                PlaceViewmodel(
                    placeRepository = placeRepository
                )
            }
        }
    }

}