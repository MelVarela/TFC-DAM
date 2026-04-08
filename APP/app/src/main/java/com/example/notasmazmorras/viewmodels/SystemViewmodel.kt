package com.example.notasmazmorras.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.notasmazmorras.data.repositories.RepositoryResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SystemViewmodel : ViewModel() {
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

    companion object {
        private var Instance : SystemViewmodel? = null

        fun getInstance() : SystemViewmodel {
            return Instance ?: synchronized(this) {
                Instance = SystemViewmodel()
                return Instance!!
            }
        }
    }
}