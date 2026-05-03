package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.data.repositories.CreatureRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreatureViewmodel(
    private val creatureRepository: CreatureRepository
) : ViewModel() {

    val creatures : StateFlow<List<LocalCreature>> = creatureRepository.getAllCreatures()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertCreature(creature: LocalCreature) = viewModelScope.launch {
        creatureRepository.insertCreature(creature)
    }

    fun updateCreature(creature: LocalCreature) = viewModelScope.launch {
        creatureRepository.updateCreature(creature)
    }

    fun deleteCreature(creature: LocalCreature) = viewModelScope.launch {
        creatureRepository.deleteCreature(creature)
    }

    fun sync(currentCampaign: String) = viewModelScope.launch {
        creatureRepository.uploadPendingChanges()
        creatureRepository.syncFromServer(currentCampaign)
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val creatureRepository = application.container.creatureRepository
                CreatureViewmodel(
                    creatureRepository = creatureRepository
                )
            }
        }
    }

}