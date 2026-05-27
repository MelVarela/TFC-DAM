package com.example.notasmazmorras.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notasmazmorras.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Banner(
    mostrarMenu: Boolean,
    goBack: Boolean,
    onBack: () -> Unit,
    onMenu: () -> Unit,
){

    CenterAlignedTopAppBar(
        title = { Image(
            painter = painterResource(R.drawable.icon),
            contentDescription = stringResource(R.string.logo)
        ) },
        navigationIcon = {
            if(mostrarMenu){
                IconButton(onClick = {onMenu()}) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = stringResource(R.string.go_back))
                }
            }
            else if(goBack){
                IconButton(onClick = {onBack()}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.go_back))
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(229, 246, 255, 255)),
        modifier = Modifier.fillMaxWidth().height(84.dp)
    )

}