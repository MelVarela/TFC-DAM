package com.example.notasmazmorras.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.model.local.LocalInventory
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.data.model.remote.DndClass
import com.example.notasmazmorras.data.repositories.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class CharacterViewmodel (
    private val characterRepository: CharacterRepository
) : ViewModel() {

    val characters : StateFlow<List<LocalCharacter>> = characterRepository.getAllCharacters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val objectsInventory : StateFlow<List<LocalInventory>> = characterRepository.getAllInventories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _clases = MutableStateFlow(emptyList<DndClass>())
    val clases : StateFlow<List<DndClass>> = _clases.asStateFlow()

    private val _objetos = MutableStateFlow(emptyList<LocalObject>())
    val objetos : StateFlow<List<LocalObject>> = _objetos.asStateFlow()

    private val _subClases = MutableStateFlow(emptyList<String>())
    val subClases = _subClases.asStateFlow()


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

    fun getClases()  = viewModelScope.launch {
        _clases.value = characterRepository.getClases()
    }

    fun getSubclasesFor(clase: String) = viewModelScope.launch {
        _subClases.value = characterRepository.getSubclasesFor(clase)
    }

    fun logOut() = viewModelScope.launch {
        characterRepository.uploadPendingChanges()
        characterRepository.reset()
    }

    fun addObjectTo(char: String, obj: String) = viewModelScope.launch {
        characterRepository.addItem(
            LocalInventory(
                character = char,
                obxecto = obj
            )
        )
    }

    fun getObjectsFor(char: String) = viewModelScope.launch {
        _objetos.value = characterRepository.getItemsOf(char)
    }

    fun deleteInventory(inv: LocalInventory) = viewModelScope.launch {
        Log.d("DB", "Deleting: $inv")
        characterRepository.deleteItem(inv)
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