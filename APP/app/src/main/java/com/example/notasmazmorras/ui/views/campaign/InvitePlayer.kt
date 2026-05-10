package com.example.notasmazmorras.ui.views.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.ui.views.campaign.details.EditCharacterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitePlayer(
    onDone: (LocalUserRelation) -> Unit,
    campaign: String,
    navController: NavController
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invite player") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
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
            InvitePlayerScreen(
                onDone = onDone,
                campaign = campaign,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun InvitePlayerScreen(
    onDone: (LocalUserRelation) -> Unit,
    campaign: String,
    modifier: Modifier
){
    var player by remember { mutableStateOf("") }

    Scaffold(
    ) { contentPadding ->

        Column(
            modifier = modifier
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            TextField(
                value = player,
                onValueChange = {player = it},
                label = { Text("Correo del jugador a invitar") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
            )
            Button(
                onClick = {
                    onDone(
                        LocalUserRelation(
                            isAccepted = false,
                            role = "p",
                            schedule = "",
                            user = player,
                            campaign = campaign
                        )
                    )
                }
            ) {
                Text("Invitar")
            }
        }
    }
}