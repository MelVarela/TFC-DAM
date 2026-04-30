package com.example.notasmazmorras.ui.views.campaign.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.ui.components.GenericCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Objects(
    obxectos: List<LocalObject>,
    onDelete: (LocalObject) -> Unit,
    onSelect: (String) -> Unit,
    onEdit: (String) -> Unit,
    onBack: () -> Unit,
    onSync: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Objects") },
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
                onClick = {navController.navigate("editObject")}
            ) {
                Text("Crear objeto")
            }
            Button(
                onClick = { onSync() }
            ) {
                Icon(Icons.Default.Sync, contentDescription = "Sync")
            }

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                items(obxectos){ obxecto ->
                    GenericCard(
                        onDelete = { onDelete(obxecto) },
                        onSelect = { onSelect(obxecto.id) },
                        onEdit = { onEdit(obxecto.id) },
                        picture = obxecto.picture,
                        name = obxecto.name
                    )
                }
            }
        }
    }
}