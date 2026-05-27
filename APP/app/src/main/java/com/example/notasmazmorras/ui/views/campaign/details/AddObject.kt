package com.example.notasmazmorras.ui.views.campaign.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notasmazmorras.R
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.ui.components.ObjectRelCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddObject(
    onBack: () -> Unit,
    objects: List<LocalObject>,
    onObjectSelected: (String) -> Unit,
    onDelete: (String) -> Unit,
    objectsInInventory: List<LocalObject>,
    navController: NavController
){

    NavigationMenu(
        mostrarMenu = false,
        goBack = true,
        onBack = {onBack()},
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
        AddObjectScreen(
            objects = objects,
            onObjectSelected = onObjectSelected,
            onDelete = onDelete,
            objectsInInventory = objectsInInventory
        )
    }

}

@Composable
fun AddObjectScreen(
    objects: List<LocalObject>,
    onObjectSelected: (String) -> Unit,
    onDelete: (String) -> Unit,
    objectsInInventory: List<LocalObject>
){

    var desplegado by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){

        Column {
            Button(
                onClick = {desplegado = !desplegado},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {Text(stringResource(R.string.add_object))}
            DropdownMenu(
                expanded = desplegado,
                onDismissRequest = {},
                modifier = Modifier.fillMaxWidth()
            ) {

                objects.forEach { obxecto ->
                    DropdownMenuItem(
                        text = {Text(obxecto.name)},
                        onClick = {
                            onObjectSelected(obxecto.id)
                            desplegado = false
                        }
                    )
                }

            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            items(objectsInInventory){ obxecto ->
                ObjectRelCard(
                    obxecto = obxecto,
                    onDelete = onDelete,
                )
            }
        }

    }

}