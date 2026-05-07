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
import com.example.notasmazmorras.data.model.remote.Suggestion
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

    private val _currentUser = MutableStateFlow("")
    val currentUser : StateFlow<String> = _currentUser.asStateFlow()

    fun firstTimeOpened() = viewModelScope.launch {
        Log.d("LAUNCH", "Getting sys data")
        try {
            if(systemRepository.getIsDataPresent().first() == 0){
                Log.d("LAUNCH", "No data detected, creating")
                systemRepository.insert(
                    SysTable(
                        "1",
                        lastUser = "",
                        LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                        "esp"
                    )
                )
            }else{
                changeLastTimeSigned()
            }
        }catch (e: Throwable){
            Log.d("DB", "No data detected by error, creating...")
            systemRepository.insert(
                SysTable(
                    "1",
                    lastUser = "",
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    "esp",
                    false
                )
            )
        }
    }

    fun setLastSigned(email: String) = viewModelScope.launch {
        Log.d("DB", "Setting last time signed as now")
        val sysDataUpdated = systemRepository.getData()
        val sysData = sysDataUpdated.first()
        _currentUser.value = email
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
        Log.d("DB", "Reseting last time signed as now")
        val sysDataUpdated = systemRepository.getData()
        val sysData = sysDataUpdated.first()
        _currentUser.value = sysData.lastUser
        systemRepository.update(
            sysData.copy(
                lastSinged = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
        )
    }

    fun checkIfStillAuthenticated() = viewModelScope.launch {
        Log.d("DB", "Checking if user still has auth")
        try{
            val sysDataUpdated = systemRepository.getData()
            val sysData = sysDataUpdated.first()
            if(!sysData.authenticated){
                _authenticated.value = false
            }else if(
                LocalDateTime.ofEpochSecond(sysData.lastSinged, 0, ZoneOffset.UTC)
                    .isBefore(LocalDateTime.now().minusDays(7))
            ){
                systemRepository.update(
                    sysData.copy(
                        authenticated = false
                    )
                )
                _authenticated.value = false
            }else{
                _authenticated.value = true
            }
        }catch (e: Throwable){
            _authenticated.value = false
        }
    }

    fun logOut() = viewModelScope.launch {
        Log.d("DB", "Logging out")
        _authenticated.value = false
        val sysDataUpdated = systemRepository.getData()
        val sysData = sysDataUpdated.first()
        _currentUser.value = ""
        systemRepository.update(
            sysData.copy(
                lastUser = "",
                authenticated = false
            )
        )
    }

    fun sendReport(report: Suggestion) = viewModelScope.launch {
        systemRepository.sendReport(report)
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