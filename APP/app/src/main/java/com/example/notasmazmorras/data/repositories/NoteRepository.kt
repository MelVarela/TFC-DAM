package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalNote
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteNote
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CampaignDao
import com.example.notasmazmorras.data.repositories.daos.NoteDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface NoteRepository {

    fun getAllNotes(): Flow<List<LocalNote>>

    suspend fun insertNote(note: LocalNote): RepositoryResult

    suspend fun updateNote(note: LocalNote): RepositoryResult

    suspend fun deleteNote(note: LocalNote): RepositoryResult

    suspend fun setEditing(note: LocalNote, state: Boolean)

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(owner: String): RepositoryResult

    suspend fun reset()
}

class DefaultNoteRepository(
    private val local : NoteDao,
    private val remote : ApiService
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
            local.update(note.copy(pendingSync = true))
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

    override suspend fun setEditing(note: LocalNote, state: Boolean) {
        remote.updateNote(
            note.copy(isEditing = state).toRemote()
        )
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        var toSync = local.getNotesToSync()

        try{
            toSync.first().map {
                val id = it.id.substring(it.id.indexOf("_") + 1, it.id.length)

                if(it.pendingDelete){
                    if(it.id.substring(0, 1) != "l") remote.deleteNote(it.id)
                    local.delete(it)
                }else if(it.id.substring(0, 1) == "l"){

                    var resposta : RemoteNote =
                        remote.createNote(it.toRemote())
                    local.delete(it)
                    local.insert(it.copy((resposta.id ?: "0"), pendingSync = false))

                }else{

                    remote.updateNote(it.toRemote())
                    local.update(it.copy(pendingSync = false))

                }
            }
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
        }

        return RepositoryResult.Success("Cambios sincronizados con éxito.")
    }

    override suspend fun syncFromServer(owner: String): RepositoryResult {
        try{
            var notes = remote.getNotes(owner)
            var ids = local.getIds()

            var notesToUpdate : List<LocalNote> = ArrayList<LocalNote>()
            var notesToInsert : List<LocalNote> = ArrayList<LocalNote>()

            notes.map {
                if(!(ids.first().contains(it.id))){
                    notesToInsert = notesToInsert.plus(it.toLocal())
                }else{
                    notesToUpdate = notesToUpdate.plus(it.toLocal())
                }
            }

            local.insertList(notesToInsert)
            local.updateList(notesToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

    override suspend fun reset() {
        local.deleteAll()
    }

}