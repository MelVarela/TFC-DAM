package com.example.notasmazmorras.ui.views.campaign.details

import com.example.notasmazmorras.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.ui.components.EmptyListShow
import com.example.notasmazmorras.ui.components.GenericCard
import com.example.notasmazmorras.ui.components.NavigationMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Creatures(
    creatures: List<LocalCreature>,
    onDelete: (LocalCreature) -> Unit,
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
            navController.navigate("editCreature")
        }
    ) {
        CreaturesScreen(
            creatures = creatures,
            onDelete = onDelete,
            onSelect = onSelect,
            onEdit = onEdit,
            onSync = onSync,
        )
    }
}

@Composable
fun CreaturesScreen(
    creatures: List<LocalCreature>,
    onDelete: (LocalCreature) -> Unit,
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

        if(creatures.isNotEmpty()){
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                items(creatures){ creature ->
                    GenericCard(
                        onDelete = { onDelete(creature) },
                        onSelect = { onSelect(creature.id) },
                        onEdit = { onEdit(creature.id) },
                        picture = creature.picture,
                        name = creature.name
                    )
                }
            }
        }else{
            EmptyListShow(stringResource(R.string.no_creatures))
        }

    }
}