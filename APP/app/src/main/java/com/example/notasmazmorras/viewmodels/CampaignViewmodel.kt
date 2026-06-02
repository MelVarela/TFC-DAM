package com.example.notasmazmorras.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.repositories.CampaignRepository
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.example.notasmazmorras.R
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.data.repositories.RepositoryResult
import com.example.notasmazmorras.data.repositories.UserRelationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CampaignViewmodel (
    private val campaignRepository: CampaignRepository,
    private val userRelationRepository: UserRelationRepository
) : ViewModel() {

    private val _currentCampaign = MutableStateFlow("")
    val currentCampaign : StateFlow<String> = _currentCampaign.asStateFlow()

    private val _isDm = MutableStateFlow(false)
    val isDm : StateFlow<Boolean> = _isDm.asStateFlow()

    val campaigns : StateFlow<List<LocalCampaign>> = campaignRepository.getAllCampaigns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val userRelations : StateFlow<List<LocalUserRelation>> = userRelationRepository.getAllUserRelations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertCampaign(campaign: LocalCampaign, user: String) = viewModelScope.launch {
        campaignRepository.insertCampaign(campaign)
        userRelationRepository.insertUserRelation(
            LocalUserRelation(
                isAccepted = true,
                role = "d",
                schedule = "",
                user = user,
                campaign = campaign.id
            )
        )
    }

    fun updateCampaign(campaign: LocalCampaign) = viewModelScope.launch {
        campaignRepository.updateCampaign(campaign)
    }

    fun updateSchedule(schedule: String, user: String) = viewModelScope.launch {
        val relation = userRelationRepository.getAllUserRelations().first().firstOrNull { (it.campaign == currentCampaign.value && it.user == user) }
        if (relation != null){
            userRelationRepository.updateUserRelation(
                relation.copy(schedule = schedule, pendingSync = true)
            )
        }
    }

    fun deleteCampaign(campaign: LocalCampaign) = viewModelScope.launch {
        campaignRepository.deleteCampaign(campaign)
    }

    fun deleteRelation(relation: LocalUserRelation) = viewModelScope.launch {
        if(relation.role != "d"){
            userRelationRepository.deleteUserRelation(relation)
            userRelationRepository.uploadPendingChanges()
            userRelationRepository.syncFromServer(relation.campaign)
        }
    }

    fun sync(emailUser: String) = viewModelScope.launch {
        campaignRepository.uploadPendingChanges()
        campaignRepository.syncFromServer(emailUser)
    }

    fun syncRelations(campaign: String) = viewModelScope.launch {
        userRelationRepository.uploadPendingChanges()
        userRelationRepository.syncFromServer(campaign)
    }

    fun createCampaign(campaign: LocalCampaign, user: String) = viewModelScope.launch {
        campaignRepository.insertCampaign(campaign)
        userRelationRepository.insertUserRelation(
            LocalUserRelation(
                isAccepted = true,
                role = "d",
                schedule = "",
                user = user,
                campaign = campaign.id
            )
        )
        var result : RepositoryResult = campaignRepository.uploadPendingChanges()
        when(result){
            is RepositoryResult.Success -> {
                userRelationRepository.uploadPendingChanges()
                campaignRepository.syncFromServer(user)
            }

            is RepositoryResult.Error -> {
                Log.d("DB", "Error creating campaing.\nCampaignViewmodel createCampaign")
            }
        }
    }

    fun syncRelationsByUser(user: String) = viewModelScope.launch {
        userRelationRepository.uploadPendingChanges()
        userRelationRepository.syncFromServerByUser(user)
    }

    fun syncPending(user: String) = viewModelScope.launch {
        userRelationRepository.syncPending(user)
    }

    fun logOut() = viewModelScope.launch {
        userRelationRepository.uploadPendingChanges()
        userRelationRepository.reset()
        campaignRepository.uploadPendingChanges()
        campaignRepository.reset()
    }

    fun setCurrentCampaign(campaign: String, user: String) = viewModelScope.launch {
        _currentCampaign.value = campaign
        if(campaign != ""){
            try{
                val relation : LocalUserRelation = userRelations.first().first {
                    it.campaign == campaign && it.user == user
                }
                if(relation.role == "d"){
                    _isDm.value = true
                }
            }catch (e: Throwable){
                _isDm.value = true
            }
        }
    }

    fun invitePlayer(userRelation: LocalUserRelation, context: Context) = viewModelScope.launch {
        userRelationRepository.invitePlayer(userRelation, context)
    }

    fun accept(invite: LocalUserRelation, context: Context) = viewModelScope.launch {
        if(userRelationRepository.isConected()){
            userRelationRepository.updateUserRelation(
                invite.copy(
                    isAccepted = true
                )
            )
            syncRelations(invite.campaign)
        }else{
            Toast.makeText(context, context.getString(R.string.no_conexion), Toast.LENGTH_LONG).show()
        }
    }

    fun reject(invite: LocalUserRelation, context: Context) = viewModelScope.launch {
        if(userRelationRepository.isConected()){
            userRelationRepository.deleteUserRelation(invite)
            syncRelations(invite.campaign)
        }else{
            Toast.makeText(context, context.getString(R.string.no_conexion), Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val campaignRepository = application.container.campaignRepository
                val userRelationRepository = application.container.userRelationRepository
                CampaignViewmodel(
                    campaignRepository = campaignRepository,
                    userRelationRepository = userRelationRepository
                )
            }
        }
    }

}