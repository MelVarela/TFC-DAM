package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.model.remote.Credentials
import com.example.notasmazmorras.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewmodel(
    private val userRepository: UserRepository,
    private val systemViewmodel: SystemViewmodel
) : ViewModel() {

    private val _authenticated = MutableStateFlow(false)
    val authenticated : StateFlow<Boolean> = _authenticated.asStateFlow()

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

    fun login(email: String, password: String) = viewModelScope.launch {
        _authenticated.value = userRepository.login(Credentials(email, password))
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