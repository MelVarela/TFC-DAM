package com.example.notasmazmorras.ui.views.campaign.details

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCharacter(navController: NavController) {
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
            Button(
                onClick = {navController.navigate("campaign/1/characters")}
            ) {
                Text("Terminar")
            }

            EditCharacterScreen(modifier = Modifier)
        }
    }
}

@Composable
fun EditCharacterScreen(
    modifier: Modifier
){

    var name by remember { mutableStateOf("") }
    var clase by remember { mutableStateOf("") }
    var subclase by remember { mutableStateOf("") }
    var maxPg by remember { mutableStateOf("") }
    var pg by remember { mutableStateOf("") }

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
        )

        TextField(
            value = pg,
            onValueChange = {pg = it},
            label = { Text("Pg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
        )

    }

}