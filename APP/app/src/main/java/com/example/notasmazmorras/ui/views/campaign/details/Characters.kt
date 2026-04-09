package com.example.notasmazmorras.ui.views.campaign.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.ui.components.CharacterCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Characters(
    characters: List<LocalCharacter>,
    onDelete: (LocalCharacter) -> Unit,
    onSelect: (String) -> Unit,
    onEdit: (String) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
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
            Button(
                onClick = {navController.navigate("details/char/1")}
            ) {
                Text("Ver personaje")
            }
            Button(
                onClick = {navController.navigate("editCharacter")}
            ) {
                Text("Crear personaje")
            }

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                items(characters){ character ->
                    CharacterCard(
                        character = character,
                        onDelete = onDelete,
                        onSelect = onSelect,
                        onEdit = onEdit,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}