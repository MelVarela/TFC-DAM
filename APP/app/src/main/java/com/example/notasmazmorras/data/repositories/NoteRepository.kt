package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalNote
import com.example.notasmazmorras.data.repositories.daos.CampaignDao
import com.example.notasmazmorras.data.repositories.daos.NoteDao
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<LocalNote>>

    suspend fun insertNote(note: LocalNote): RepositoryResult

    suspend fun updateNote(note: LocalNote): RepositoryResult

    suspend fun deleteNote(note: LocalNote): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(): RepositoryResult
}

class DefaultNoteRepository(
    private val local : NoteDao,
    private val remote : Object //ApiService
) : NoteRepository {

    final val TAG = "note_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllNotes(): Flow<List<LocalNote>> = local.getAllNotes()

    override suspend fun insertNote(note: LocalNote): RepositoryResult {
        try{
            local.insert(note.copy(pendingSync = true))
            return RepositoryResult.Success("Nota '${note.name}' creada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error creando la nota '${note.name}'.")
        }
    }

    override suspend fun updateNote(note: LocalNote): RepositoryResult {
        try{
            local.update(note)
            return RepositoryResult.Success("Nota '${note.name}' actualizada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            if(note.pendingDelete) return RepositoryResult.Error("Error eliminando la nota '${note.name}'.")
            else return RepositoryResult.Error("Error actualizando la nota '${note.name}'.")
        }
    }

    override suspend fun deleteNote(note: LocalNote): RepositoryResult {
        try{
            updateNote(note.copy(pendingSync = true, pendingDelete = true))
            return RepositoryResult.Success("Nota '${note.name}' eliminada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error eliminando la nota '${note.name}'.")
        }
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        throw Exception("To Do")
    }

    override suspend fun syncFromServer(): RepositoryResult {
        throw Exception("To Do")
    }

}