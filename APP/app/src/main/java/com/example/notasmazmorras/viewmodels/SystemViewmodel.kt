package com.example.notasmazmorras.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.SysTable
import com.example.notasmazmorras.data.repositories.SystemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class SystemViewmodel(
    private val systemRepository: SystemRepository
) : ViewModel() {

    private val _authenticated = MutableStateFlow(false)
    val authenticated : StateFlow<Boolean> = _authenticated.asStateFlow()

    fun firstTimeOpened() = viewModelScope.launch {
        if(systemRepository.getIsDataPresent().first() == 0){
            systemRepository.insert(
                SysTable(
                    "1",
                    "",
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    "esp"
                )
            )
        }else{
            changeLastTimeSigned()
        }
    }

    fun setLastSigned(email: String) = viewModelScope.launch {
        val sysDataUpdated = systemRepository.getData()
        val sysData = sysDataUpdated.first()
        systemRepository.update(
            sysData.copy(
                lastSinged = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                lastUser = email,
                authenticated = true,
                language = sysData.language
            )
        )
    }

    fun changeLastTimeSigned() = viewModelScope.launch {
        val sysDataUpdated = systemRepository.getData()
        val sysData = sysDataUpdated.first()
        systemRepository.update(
            sysData.copy(
                lastSinged = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                lastUser = sysData.lastUser,
                authenticated = sysData.authenticated,
                language = sysData.language
            )
        )
    }

    fun checkIfStillAuthenticated() = viewModelScope.launch {
        val sysDataUpdated = systemRepository.getData()
        val sysData = sysDataUpdated.first()
        if(!sysData.authenticated){
            _authenticated.value = false
        }else if(
            LocalDateTime.ofEpochSecond(sysData.lastSinged, 0, ZoneOffset.UTC)
            .isBefore(LocalDateTime.now().minusDays(7))
        ){
            _authenticated.value = false
        }else{
            _authenticated.value = true
        }
    }

    /*
    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    suspend fun processResult(result: RepositoryResult) {
        when (result) {
            is RepositoryResult.Success ->
                if(result.message != "") _events.send(result.message)

            is RepositoryResult.Error ->
                if(result.message != "") _events.send("${result.message} ${result.exception?.message ?: ""}".trim())
        }
    }
    */

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val systemRepository = application.container.systemRepository
                SystemViewmodel(
                    systemRepository = systemRepository
                )
            }
        }
    }
}