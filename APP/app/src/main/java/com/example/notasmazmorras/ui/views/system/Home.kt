package com.example.notasmazmorras.ui.views.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
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
                onClick = {navController.navigate("createCampaing")}
            ) {
                Text("Crear campaña")
            }
            Button(
                onClick = {navController.navigate("account/1")}
            ) {
                Text("Ver cuenta")
            }
            Button(
                onClick = {navController.navigate("campaign/1")}
            ) {
                Text("Ver campaña")
            }
            Button(
                onClick = {navController.navigate("invitations")}
            ) {
                Text("Ver invitaciones")
            }
            Button(
                onClick = {navController.navigate("reportingSugestions/report")}
            ) {
                Text("Reportar sugerencia")
            }
            Button(
                onClick = {navController.navigate("options")}
            ) {
                Text("Opciones")
            }
        }
    }
}