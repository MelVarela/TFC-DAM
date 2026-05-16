package com.example.notasmazmorras.ui.views.campaign

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
import com.example.notasmazmorras.R
import com.example.notasmazmorras.data.model.local.LocalNote
import com.example.notasmazmorras.ui.components.NoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(
    notes: List<LocalNote>,
    onPress: (String) -> Unit,
    onDelete: (LocalNote) -> Unit,
    onNew: () -> Unit,
    onSync: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.notes)) },
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
                onClick = { onNew() }
            ) {
                Text(stringResource(R.string.new_note))
            }
            Button(
                onClick = { onSync() }
            ) {
                Icon(Icons.Default.Sync, contentDescription = "Sync")
            }

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                items(notes){ note ->
                    NoteCard(
                        note = note,
                        onPress = onPress,
                        onDelete = onDelete,
                    )
                }
            }
        }

    }
}