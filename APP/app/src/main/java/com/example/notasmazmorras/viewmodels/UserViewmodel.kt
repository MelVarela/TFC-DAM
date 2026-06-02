package com.example.notasmazmorras.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.model.UserAccount
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.model.remote.Credentials
import com.example.notasmazmorras.data.repositories.CreateUserResult
import com.example.notasmazmorras.data.repositories.ImageUploadResult
import com.example.notasmazmorras.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.Boolean

class UserViewmodel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authenticated = MutableStateFlow(false)
    val authenticated : StateFlow<Boolean> = _authenticated.asStateFlow()

    val users : StateFlow<List<LocalUser>> = userRepository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _createState = MutableStateFlow(CreateState())
    val createState : StateFlow<CreateState> = _createState.asStateFlow()

    fun insertUser(user: UserAccount) = viewModelScope.launch {
        userRepository.insertUser(user)
    }

    fun updateUser(user: LocalUser) = viewModelScope.launch {
        userRepository.updateUser(user)
    }

    fun deleteUser(user: LocalUser) = viewModelScope.launch {
        userRepository.deleteUser(user)
    }

    fun sync(email: String) = viewModelScope.launch {
        userRepository.uploadPendingChanges()
        userRepository.syncFromServer(email)
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        try{
            _authenticated.value = userRepository.login(Credentials(email, password))
            userRepository.syncFromServer(email)
        }catch(e: Throwable){
            Log.d("ERR", "Bad password.")
        }

    }

    fun createUser(user: UserAccount){

        _createState.value = _createState.value.copy(
            isLoading = true,
            creationStarted = true
        )

        viewModelScope.launch {
            val resultado = userRepository.createUser(user)
            when(resultado){

                is CreateUserResult.Success -> {

                    _createState.update {
                        it.copy(
                            isLoading = false,
                            creationStarted = false,
                            created = true,
                            badEmail = resultado.badEmail
                        )
                    }

                }

                is CreateUserResult.Error -> {

                    _createState.update {
                        it.copy(
                            isLoading = false,
                            creationStarted = false,
                            badEmail = resultado.badEmail
                        )
                    }

                }

            }
        }

    }

    fun finishCreation() {
        _createState.update {
            it.copy(
                badEmail = false,
                created = false,
                isLoading = false,
                creationStarted = false,
            )
        }
    }

    fun logOut() = viewModelScope.launch {
        userRepository.reset()
        _authenticated.value = false
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val userRepository = application.container.userRepository
                UserViewmodel(
                    userRepository = userRepository
                )
            }
        }
    }

}

data class CreateState(
    val badEmail : Boolean = false,
    val created : Boolean = false,
    val isLoading : Boolean = false,
    val creationStarted : Boolean = false
)