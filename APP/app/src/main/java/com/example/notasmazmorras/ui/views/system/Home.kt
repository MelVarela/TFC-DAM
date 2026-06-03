package com.example.notasmazmorras.ui.views.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.ui.components.CampaignCard
import com.example.notasmazmorras.ui.components.EmptyListShow
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.R
import com.example.notasmazmorras.data.model.local.LocalUserRelation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    campaigns: List<LocalCampaign>,
    onDelete: (LocalCampaign) -> Unit,
    onSelect: (String) -> Unit,
    onSync: () -> Unit,
    userRelations: List<LocalUserRelation>,
    currentUser: String,
    navController: NavController
) {
    NavigationMenu(
        mostrarMenu = true,
        goBack = false,
        onBack = {},
        navController = navController,
        floatingAction = true,
        onFloating = {
            navController.navigate("createCampaign")
        }
    ) {
        HomeScreen(
            campaigns = campaigns,
            onDelete = onDelete,
            onSelect = onSelect,
            onSync = onSync,
            userRelations = userRelations,
            currentUser = currentUser,
        )
    }
}

@Composable
fun HomeScreen(
    campaigns: List<LocalCampaign>,
    onDelete: (LocalCampaign) -> Unit,
    onSelect: (String) -> Unit,
    onSync: () -> Unit,
    userRelations: List<LocalUserRelation>,
    currentUser: String,
){

    var firstLoad by remember { mutableStateOf(true) }
    if(firstLoad){
        firstLoad = false
        //onSync()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    horizontal = 16.dp
                )
            )
    ){

        Button(
            onClick = { onSync() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Icon(Icons.Default.Sync, contentDescription = "Sync")
        }

        if(campaigns.isNotEmpty()){
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                items(campaigns) { campaign ->
                    CampaignCard(
                        campaign = campaign,
                        isDm = (userRelations.firstOrNull { it.campaign == campaign.id && it.user == currentUser }?.role == "d") ?: false,
                        onDelete = onDelete,
                        onSelect = onSelect
                    )
                }
            }
        }else{
            EmptyListShow(stringResource(R.string.no_campaigns))
        }

    }

}