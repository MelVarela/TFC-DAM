package com.example.notasmazmorras.ui.views.campaign.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCreature

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCreature(
    onDone: (LocalCreature) -> Unit,
    creatures: List<LocalCreature>,
    creatureId: String?,
    campaign: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Creature") },
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
                onClick = {navController.navigate("campaign/1/creatures")}
            ) {
                Text("Terminar")
            }

            EditCreatureScreen(
                onDone = onDone,
                creatures = creatures,
                creatureId = creatureId,
                campaign = campaign,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun EditCreatureScreen(
    onDone: (LocalCreature) -> Unit,
    creatures: List<LocalCreature>,
    creatureId: String?,
    campaign: String,
    modifier: Modifier
){

    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }

    if(creatureId != null){
        val creature : LocalCreature = creatures.first{it.id == creatureId}
        name = creature.name
        species = creature.species
    }

    Scaffold { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){

            TextField(
                value = name,
                onValueChange = {name = it},
                label = { Text("Nombre") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
            )

            TextField(
                value = species,
                onValueChange = {species = it},
                label = { Text("Especie") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
            )

            Button(
                onClick = {
                    if(creatureId != null){
                        onDone(LocalCreature(
                            creatureId,
                            name,
                            species,
                            "",
                            campaign,
                            true
                        ))
                    }else{
                        onDone(LocalCreature(
                            "local_${System.nanoTime()}crea",
                            name,
                            species,
                            "",
                            campaign,
                            true
                        ))
                    }
                }
            ) { Text("Done") }
        }
    }
}