package com.example.notasmazmorras.data.repositories

sealed class RepositoryResult {
    data class Success(val message: String) : RepositoryResult()

    data class Error(val message: String, val exception: Throwable? = null) : RepositoryResult()
}