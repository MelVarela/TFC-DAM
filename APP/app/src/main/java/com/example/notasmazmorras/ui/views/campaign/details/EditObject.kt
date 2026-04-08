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
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditObject(
    onDone: (LocalObject) -> Unit,
    objects: List<LocalObject>,
    objectId: String?,
    campaign: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Object") },
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
                onClick = {navController.navigate("campaign/1/objects")}
            ) {
                Text("Terminar")
            }

            EditObjectScreen(
                onDone = onDone,
                objects = objects,
                objectId = objectId,
                campaign = campaign,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun EditObjectScreen(
    onDone: (LocalObject) -> Unit,
    objects: List<LocalObject>,
    objectId: String?,
    campaign: String,
    modifier : Modifier
){

    var name by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }

    if(objectId != null){
        val obxecto : LocalObject = objects.first{it.id == objectId}
        name = obxecto.name
        cost = obxecto.cost.toString()
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
            value = cost,
            onValueChange = {cost = it},
            label = { Text("Costo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        Button(onClick = {
            if(objectId != null){
                onDone(LocalObject(
                    objectId,
                    name,
                    cost.toFloat(),
                    "",
                    campaign,
                    true
                ))
            }else{
                onDone(LocalObject(
                    "local_${System.nanoTime()}obje",
                    name,
                    cost.toFloat(),
                    "",
                    campaign,
                    true
                ))
            }
        }) { Text("Done") }

    }

}