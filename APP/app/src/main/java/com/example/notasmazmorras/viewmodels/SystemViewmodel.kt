package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.data.repositories.RepositoryResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SystemViewmodel : ViewModel() {
    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    suspend fun processResult(result: RepositoryResult) {
        when (result) {
            is RepositoryResult.Success ->
                _events.send(result.message)

            is RepositoryResult.Error ->
                _events.send("${result.message} ${result.exception?.message ?: ""}".trim())
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SystemViewmodel()
            }
        }
    }
}