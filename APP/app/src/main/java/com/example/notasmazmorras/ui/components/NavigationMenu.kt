package com.example.notasmazmorras.ui.components

import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notasmazmorras.R
import kotlinx.coroutines.launch

@Composable
fun NavigationMenu(
    mostrarMenu: Boolean,
    goBack: Boolean,
    onBack: () -> Unit,
    navController: NavController,
    floatingAction: Boolean,
    onFloating: () -> Unit,
    content: @Composable () -> Unit,
){

    fun navigateTo(to: Int) {
        when(to){
            0 -> {
                navController.navigate("home")
            }
            1 -> {
                navController.navigate("invitations")
            }
            2 -> {
                navController.navigate("account/0")
            }
            3 -> {
                navController.navigate("reportingSuggestions/R")
            }
            4 -> {
                navController.navigate("reportingSuggestions/S")
            }
            5 -> {
                navController.navigate("options")
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(
        stringResource(R.string.campaigns), stringResource(R.string.invitations), stringResource(R.string.account),
        stringResource(R.string.report_error), stringResource(R.string.make_suggestion), stringResource(R.string.options)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier
                    .padding(4.dp)
            ){
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    IconButton(onClick = {scope.launch { drawerState.close() }}) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = stringResource(R.string.go_back))
                    }
                    Text("Notas & Mazmorras")
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){


                    HorizontalDivider()

                    items.forEachIndexed { index, label ->
                        NavigationDrawerItem(
                            label = {
                                Box(
                                    modifier = Modifier.background(
                                        Color(5, 35, 51, 255)
                                    )
                                    .padding(8.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color(5, 35, 51, 255)
                                    )
                                ){
                                    Text(
                                        text = label,
                                        color = Color.White
                                    )
                                }
                            },
                            selected = false,
                            onClick = {
                                navigateTo(index)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                }

            }

        }
    ){

        Scaffold(
            topBar = {
                Banner(
                    mostrarMenu = mostrarMenu,
                    goBack = goBack,
                    onBack = onBack,
                    onMenu = {scope.launch { drawerState.open() }}
                )
            },
            floatingActionButton = {
                if (floatingAction){
                    FloatingActionButton(
                        onClick = { onFloating() },
                    ) {
                        Icon(Icons.Filled.Add, "Floating action button.")
                    }
                }
            }
        ){ padding ->
            Box(
                modifier = Modifier.padding(padding)
            ){
                content()
            }
        }

    }
}