package com.example.notasmazmorras.ui.views.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCampaign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Campaign(
    campaign: LocalCampaign?,
    onBack: () -> Unit,
    onSync: () -> Unit,
    onUserRelations: (String) -> Unit,
    navController: NavController
) {

    if(campaign == null) {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(campaign?.name ?: "Error") },
                navigationIcon = {
                    IconButton(onClick = {onBack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ){ contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/calendar")}
            ) {
                Text("Ver calendario")
            }
            Button(
                onClick = {onUserRelations("campaign/${campaign!!.id}/players")}
            ) {
                Text("Ver jugadores")
            }
            Button(
                onClick = {navController.navigate("notes/${campaign!!.id}/notes")}
            ) {
                Text("Ver notas")
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/characters")}
            ) {
                Text("Ver personajes")
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/objects")}
            ) {
                Text("Ver objetos")
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/creatures")}
            ) {
                Text("Ver criaturas")
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/map")}
            ) {
                Text("Ver mapa")
            }
            Button(
                onClick = { onSync() }
            ) {
                Icon(Icons.Default.Sync, contentDescription = "Sync")
            }
        }
    }
}