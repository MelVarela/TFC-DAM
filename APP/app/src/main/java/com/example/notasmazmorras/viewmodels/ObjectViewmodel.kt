package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.data.repositories.ObjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ObjectViewmodel(
    private val objectRepository: ObjectRepository,
    private val systemViewmodel: SystemViewmodel
) : ViewModel() {

    val objects : StateFlow<List<LocalObject>> = objectRepository.getAllObjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertObject(obxecto: LocalObject) = viewModelScope.launch {
        systemViewmodel.processResult(objectRepository.insertObject(obxecto))
    }

    fun updateObject(obxecto: LocalObject) = viewModelScope.launch {
        systemViewmodel.processResult(objectRepository.updateObject(obxecto))
    }

    fun deleteObject(obxecto: LocalObject) = viewModelScope.launch {
        systemViewmodel.processResult(objectRepository.deleteObject(obxecto))
    }

    fun sync() = viewModelScope.launch {
        objectRepository.uploadPendingChanges()
        objectRepository.syncFromServer()
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val objectRepository = application.container.objectRepository
                ObjectViewmodel(
                    objectRepository = objectRepository,
                    systemViewmodel = SystemViewmodel()
                )
            }
        }
    }

}