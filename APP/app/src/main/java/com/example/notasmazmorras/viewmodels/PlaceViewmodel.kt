package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalPlace
import com.example.notasmazmorras.data.repositories.PlaceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaceViewmodel(
    private val placeRepository: PlaceRepository,
    private val systemViewmodel: SystemViewmodel
) : ViewModel() {

    val places : StateFlow<List<LocalPlace>> = placeRepository.getAllPlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertCampaign(place: LocalPlace) = viewModelScope.launch {
        systemViewmodel.processResult(placeRepository.insertPlace(place))
    }

    fun updateCampaign(place: LocalPlace) = viewModelScope.launch {
        systemViewmodel.processResult(placeRepository.updatePlace(place))
    }

    fun deleteCampaign(place: LocalPlace) = viewModelScope.launch {
        systemViewmodel.processResult(placeRepository.deletePlace(place))
    }

    fun sync() = viewModelScope.launch {
        placeRepository.uploadPendingChanges()
        placeRepository.syncFromServer()
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val placeRepository = application.container.placeRepository
                PlaceViewmodel(
                    placeRepository = placeRepository,
                    systemViewmodel = SystemViewmodel()
                )
            }
        }
    }

}