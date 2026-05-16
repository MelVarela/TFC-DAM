package com.example.notasmazmorras.ui.views.campaign.details

import com.example.notasmazmorras.R
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.ui.components.GenericCard

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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.creatures)) },
                navigationIcon = {
                    IconButton(onClick = {onBack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.go_back))
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
                onClick = {navController.navigate("editCreature")}
            ) {
                Text(stringResource(R.string.add_creature))
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
        }
    }
}