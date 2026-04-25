package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.repositories.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewmodel(
    private val userRepository: UserRepository,
    private val systemViewmodel: SystemViewmodel
) : ViewModel() {

    val users : StateFlow<List<LocalUser>> = userRepository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertUser(user: LocalUser) = viewModelScope.launch {
        systemViewmodel.processResult(userRepository.insertUser(user))
    }

    fun updateUser(user: LocalUser) = viewModelScope.launch {
        systemViewmodel.processResult(userRepository.updateUser(user))
    }

    fun deleteUser(user: LocalUser) = viewModelScope.launch {
        systemViewmodel.processResult(userRepository.deleteUser(user))
    }

    fun sync(email: String) = viewModelScope.launch {
        userRepository.uploadPendingChanges()
        userRepository.syncFromServer(email)
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val userRepository = application.container.userRepository
                UserViewmodel(
                    userRepository = userRepository,
                    systemViewmodel = SystemViewmodel.getInstance()
                )
            }
        }
    }

}