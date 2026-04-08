package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalNote
import com.example.notasmazmorras.data.repositories.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewmodel(
    private val noteRepository: NoteRepository,
    private val systemViewmodel: SystemViewmodel
) : ViewModel() {

    val notes : StateFlow<List<LocalNote>> = noteRepository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertNote(note: LocalNote) = viewModelScope.launch {
        systemViewmodel.processResult(noteRepository.insertNote(note))
    }

    fun updateNote(note: LocalNote) = viewModelScope.launch {
        systemViewmodel.processResult(noteRepository.updateNote(note))
    }

    fun deleteNote(note: LocalNote) = viewModelScope.launch {
        systemViewmodel.processResult(noteRepository.deleteNote(note))
    }

    fun sync() = viewModelScope.launch {
        noteRepository.uploadPendingChanges()
        noteRepository.syncFromServer()
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val noteRepository = application.container.noteRepository
                NoteViewmodel(
                    noteRepository = noteRepository,
                    systemViewmodel = SystemViewmodel.getInstance()
                )
            }
        }
    }

}