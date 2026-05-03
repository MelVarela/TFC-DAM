package com.example.notasmazmorras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notasmazmorras.NotasMazmorrasApplication
import com.example.notasmazmorras.data.repositories.CampaignRepository
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.example.notasmazmorras.data.model.local.LocalCampaign
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CampaignViewmodel (
    private val campaignRepository: CampaignRepository
) : ViewModel() {

    val campaigns : StateFlow<List<LocalCampaign>> = campaignRepository.getAllCampaigns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    var currentCampaign : String = ""

    fun insertCampaign(campaign: LocalCampaign) = viewModelScope.launch {
        campaignRepository.insertCampaign(campaign)
    }

    fun updateCampaign(campaign: LocalCampaign) = viewModelScope.launch {
        campaignRepository.updateCampaign(campaign)
    }

    fun deleteCampaign(campaign: LocalCampaign) = viewModelScope.launch {
        campaignRepository.deleteCampaign(campaign)
    }

    fun sync(emailUser: String) = viewModelScope.launch {
        campaignRepository.uploadPendingChanges()
        campaignRepository.syncFromServer(emailUser)
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NotasMazmorrasApplication)
                val campaignRepository = application.container.campaignRepository
                CampaignViewmodel(
                    campaignRepository = campaignRepository
                )
            }
        }
    }

}