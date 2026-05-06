package com.example.notasmazmorras.ui.views.campaign

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
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
import com.example.notasmazmorras.data.model.local.LocalNote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Note(
    note: LocalNote?,
    onLoad: () -> Unit,
    onBack: (LocalNote) -> Unit,
    owner: String,
    isDm: Boolean
) {

    var firstLoad by remember { mutableStateOf(true) };
    if(firstLoad){
        onLoad()
        firstLoad = false
    }

    var name by remember { mutableStateOf("Nueva nota") }
    var content by remember { mutableStateOf("") }
    var dmOnly by remember { mutableStateOf(false) }

    if(note != null){
        name = note.name
        content = note.content
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note") },
                navigationIcon = {
                    IconButton(onClick = {

                        if (note == null){
                            onBack(LocalNote(
                                id = "local_${System.nanoTime()}note",
                                name = name,
                                content = content,
                                isDm = dmOnly,
                                isEditing = false,
                                owner = owner,
                                pendingSync = true
                            ))
                        }else{
                            onBack(LocalNote(
                                id = note.id,
                                name = name,
                                content = content,
                                isDm = dmOnly,
                                isEditing = false,
                                owner = note.owner,
                                pendingSync = true
                            ))
                        }

                    }) {
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
            if(isDm){
                Checkbox(
                    checked = dmOnly,
                    onCheckedChange = { dmOnly = it }
                )
                Text("Make note only visible to DM")
            }
            TextField(
                value = name,
                onValueChange = {name = it},
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true)
            TextField(
                value = content,
                onValueChange = {content = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
        }
    }
}