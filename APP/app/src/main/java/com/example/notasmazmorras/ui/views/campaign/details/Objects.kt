package com.example.notasmazmorras.ui.views.campaign.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.ui.components.GenericCard
import com.example.notasmazmorras.R
import com.example.notasmazmorras.ui.components.EmptyListShow
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.ui.views.system.HomeScreen

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
    NavigationMenu(
        mostrarMenu = false,
        goBack = true,
        onBack = {onBack()},
        navController = navController,
        floatingAction = true,
        onFloating = {
            navController.navigate("editObject")
        }
    ) {
        ObjectsScreen(
            obxectos = obxectos,
            onDelete = onDelete,
            onSelect = onSelect,
            onEdit = onEdit,
            onSync = onSync
        )
    }
}

@Composable
fun ObjectsScreen(
    obxectos: List<LocalObject>,
    onDelete: (LocalObject) -> Unit,
    onSelect: (String) -> Unit,
    onEdit: (String) -> Unit,
    onSync: () -> Unit,
){

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    horizontal = 16.dp
                )
            )
    ){
        Button(
            onClick = { onSync() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Icon(Icons.Default.Sync, contentDescription = "Sync")
        }

        if(obxectos.isNotEmpty()){
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
        }else{
            EmptyListShow(stringResource(R.string.no_objects))
        }

    }

}