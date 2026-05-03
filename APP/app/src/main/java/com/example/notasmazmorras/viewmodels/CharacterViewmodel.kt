package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.repositories.CharacterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewmodel (
    private val characterRepository: CharacterRepository
) : ViewModel() {

    val characters : StateFlow<List<LocalCharacter>> = characterRepository.getAllCharacters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertCharacter(character: LocalCharacter) = viewModelScope.launch {
        characterRepository.insertCharacter(character)
    }

    fun updateCharacter(character: LocalCharacter) = viewModelScope.launch {
        characterRepository.updateCharacter(character)
    }

    fun deleteCharacter(character: LocalCharacter) = viewModelScope.launch {
        characterRepository.deleteCharacter(character)
    }

    fun sync(currentCampaign: String) = viewModelScope.launch {
        characterRepository.uploadPendingChanges()
        characterRepository.syncFromServer(currentCampaign)
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val characterRepository = application.container.characterRepository
                CharacterViewmodel(
                    characterRepository = characterRepository
                )
            }
        }
    }

}