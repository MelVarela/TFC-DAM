package com.example.notasmazmorras.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.local.SysTable
import com.example.notasmazmorras.data.model.remote.Suggestion
import com.example.notasmazmorras.data.repositories.ImageUploadResult
import com.example.notasmazmorras.data.repositories.SystemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
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

    private val _uploadState = MutableStateFlow(UploadState())
    val uploadState : StateFlow<UploadState> = _uploadState.asStateFlow()

    fun firstTimeOpened() = viewModelScope.launch {
        try {
            if(systemRepository.getIsDataPresent().first() == 0){
                systemRepository.insert(
                    SysTable(
                        "1",
                        lastUser = "",
                        LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                        "es"
                    )
                )
            }else{
                changeLastTimeSigned()
            }
        }catch (e: Throwable){
            systemRepository.insert(
                SysTable(
                    "1",
                    lastUser = "",
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    "es",
                    false
                )
            )
        }
    }

    fun setLastSigned(email: String) = viewModelScope.launch {
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

    fun uploadImage(image: Bitmap){

        _uploadState.value = _uploadState.value.copy(
            uploadStarted = true,
            isLoading = true,
            url = ""
        )

        viewModelScope.launch {
            val resultado = systemRepository.uploadImage(image)
            when(resultado){

                is ImageUploadResult.Success -> {

                    _uploadState.update {
                        it.copy(
                            isLoading = false,
                            url = resultado.url,
                            error = null
                        )
                    }

                }

                is ImageUploadResult.Error -> {

                    _uploadState.update {
                        it.copy(
                            isLoading = false,
                            error = resultado.message,
                            url = ""
                        )
                    }

                }

            }
        }

    }

    fun finishUpload() {
        _uploadState.update {
            it.copy(
                url = "",
                error = null,
                isLoading = false,
                uploadStarted = false
            )
        }
    }

    fun changeLanguage(lang: String) = viewModelScope.launch {
        val sysDataUpdated = systemRepository.getData()
        val sysData = sysDataUpdated.first()
        systemRepository.update(
            sysData.copy(
                language = lang
            )
        )
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

data class UploadState(
    val url : String = "",
    val error : String? = null,
    val isLoading : Boolean = false,
    val uploadStarted : Boolean = false
)