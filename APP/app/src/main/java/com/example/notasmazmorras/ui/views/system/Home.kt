package com.example.notasmazmorras.ui.views.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.ui.components.CampaignCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    campaigns: List<LocalCampaign>,
    onDelete: (LocalCampaign) -> Unit,
    onSelect: (String) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ){ contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = {navController.navigate("createCampaign")}
            ) {
                Text("Crear campaña")
            }
            Button(
                onClick = {navController.navigate("account/1")}
            ) {
                Text("Ver cuenta")
            }
            Button(
                onClick = {navController.navigate("invitations")}
            ) {
                Text("Ver invitaciones")
            }
            Button(
                onClick = {navController.navigate("reportingSuggestions/report")}
            ) {
                Text("Reportar sugerencia")
            }
            Button(
                onClick = {navController.navigate("options")}
            ) {
                Text("Opciones")
            }

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                items(campaigns) { campaign ->
                    CampaignCard(
                        campaign = campaign,
                        onDelete = onDelete,
                        onSelect = onSelect
                    )
                }
            }
        }
    }
}