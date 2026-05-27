package com.example.notasmazmorras.ui.views.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.notasmazmorras.R
import com.example.notasmazmorras.ui.components.NavigationMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Options(
    onLogOut: () -> Unit,
    navController: NavController
) {

    NavigationMenu(
        mostrarMenu = true,
        goBack = false,
        onBack = {},
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
        OptionsScreen(
            onLogOut = onLogOut
        )
    }
}

@Composable
fun OptionsScreen(
    onLogOut: () -> Unit
){

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            16.dp
        )
    ){
        Button(
            onClick = { onLogOut() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.log_out))
        }
    }

}