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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.R

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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.go_back))
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
                Text(stringResource(R.string.calendar))
            }
            Button(
                onClick = {onUserRelations("campaign/${campaign!!.id}/players")}
            ) {
                Text(stringResource(R.string.players))
            }
            Button(
                onClick = {navController.navigate("notes/${campaign!!.id}/notes")}
            ) {
                Text(stringResource(R.string.notes))
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/characters")}
            ) {
                Text(stringResource(R.string.characters))
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/objects")}
            ) {
                Text(stringResource(R.string.objects))
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/creatures")}
            ) {
                Text(stringResource(R.string.creatures))
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/map")}
            ) {
                Text(stringResource(R.string.map))
            }
            Button(
                onClick = { onSync() }
            ) {
                Icon(Icons.Default.Sync, contentDescription = "Sync")
            }
        }
    }
}