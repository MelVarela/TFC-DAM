package com.example.notasmazmorras.ui.views.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.ui.components.RelationCard
import com.example.notasmazmorras.R
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.ui.views.system.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Players(
    userRelations: List<LocalUserRelation>,
    onDelete: (LocalUserRelation) -> Unit,
    onUserSelected: (String) -> Unit,
    onBack: () -> Unit,
    navController: NavController
) {
    NavigationMenu(
        mostrarMenu = false,
        goBack = true,
        onBack = onBack,
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
        PlayersScreen(
            userRelations = userRelations,
            onDelete = onDelete,
            onUserSelected = onUserSelected,
            navController = navController
        )
    }
}

@Composable
fun PlayersScreen(
    userRelations: List<LocalUserRelation>,
    onDelete: (LocalUserRelation) -> Unit,
    onUserSelected: (String) -> Unit,
    navController: NavController
){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            onClick = {navController.navigate("invitePlayer")},
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.invite_player))
        }
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(userRelations){ relation ->
                RelationCard(
                    userRelation = relation,
                    onDelete = onDelete,
                    onUserSelected = onUserSelected,
                    modifier = Modifier
                )
            }
        }
    }
}