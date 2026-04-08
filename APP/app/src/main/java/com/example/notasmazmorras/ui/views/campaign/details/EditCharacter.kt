package com.example.notasmazmorras.ui.views.campaign.details

import android.util.Log
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
import com.example.notasmazmorras.data.model.local.LocalCharacter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCharacter(
    onDone: (LocalCharacter) -> Unit,
    characters: List<LocalCharacter>,
    characterId: String?,
    campaign: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Character") },
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
            EditCharacterScreen(
                onDone = onDone,
                characterId = characterId,
                characters = characters,
                campaign = campaign,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun EditCharacterScreen(
    onDone: (LocalCharacter) -> Unit,
    characters: List<LocalCharacter>,
    characterId : String?,
    campaign: String,
    modifier: Modifier
){

    var name by remember { mutableStateOf("") }
    var clase by remember { mutableStateOf("") }
    var subclase by remember { mutableStateOf("") }
    var maxPg by remember { mutableStateOf("") }
    var pg by remember { mutableStateOf("") }

    if(characterId != null){
        val character : LocalCharacter = characters.first {it.id == characterId}
        name = character.name
        clase = character.clase
        subclase = character.subClase
        maxPg = character.maxPg.toString()
        pg = character.pg.toString()
    }

    Column(
        modifier = modifier,
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
            value = clase,
            onValueChange = {clase = it},
            label = { Text("Clase") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
        )

        TextField(
            value = subclase,
            onValueChange = {subclase = it},
            label = { Text("Subclase") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
        )

        TextField(
            value = maxPg,
            onValueChange = {maxPg = it},
            label = { Text("PG Maximos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        TextField(
            value = pg,
            onValueChange = {pg = it},
            label = { Text("Pg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        Button(
            onClick = {
                if(characterId != null){
                    onDone(LocalCharacter(
                        characterId,
                        name,
                        clase,
                        subclase,
                        maxPg.toInt(),
                        pg.toInt(),
                        "",
                        campaign,
                        true
                    ))
                }else{
                    onDone(LocalCharacter(
                        "local_${System.nanoTime()}char",
                        name,
                        clase,
                        subclase,
                        maxPg.toInt(),
                        pg.toInt(),
                        "",
                        campaign,
                        true
                    ))
                }
            }
        ) { Text("Done") }

    }

}