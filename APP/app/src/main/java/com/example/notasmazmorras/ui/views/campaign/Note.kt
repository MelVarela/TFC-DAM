package com.example.notasmazmorras.ui.views.campaign

import com.example.notasmazmorras.R
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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

    val context = LocalContext.current

    var firstLoad by remember { mutableStateOf(true) };
    if(firstLoad){
        onLoad()
        firstLoad = false
    }

    var name by remember { mutableStateOf(context.getString(R.string.new_note)) }
    var content by remember { mutableStateOf("") }
    var dmOnly by remember { mutableStateOf(false) }

    if(note != null){
        name = note.name
        content = note.content
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.note)) },
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
            if(isDm){
                Checkbox(
                    checked = dmOnly,
                    onCheckedChange = { dmOnly = it }
                )
                Text(stringResource(R.string.dm_view_only))
            }
            TextField(
                value = name,
                onValueChange = {name = it},
                label = { Text(stringResource(R.string.name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true)
            TextField(
                value = content,
                onValueChange = {content = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
        }
    }
}