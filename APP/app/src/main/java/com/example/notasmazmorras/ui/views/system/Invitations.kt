package com.example.notasmazmorras.ui.views.system

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.ui.components.EmptyListShow
import com.example.notasmazmorras.ui.components.InvitationCard
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Invitations(
    invitations: List<LocalUserRelation>,
    onAccepted: (LocalUserRelation, Context) -> Unit,
    onRejected: (LocalUserRelation, Context) -> Unit,
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
        InvitationScreen(
            invitations = invitations,
            onAccepted = onAccepted,
            onRejected = onRejected
        )
    }
}

@Composable
fun InvitationScreen(
    invitations: List<LocalUserRelation>,
    onAccepted: (LocalUserRelation, Context) -> Unit,
    onRejected: (LocalUserRelation, Context) -> Unit,
){

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ){

        if(invitations.isNotEmpty()){
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(invitations){ invitation ->
                    InvitationCard(
                        invitation,
                        onAccepted = onAccepted,
                        onRejected = onRejected,
                        modifier = Modifier
                    )
                }
            }
        }else{
            EmptyListShow(stringResource(R.string.no_invites))
        }
    }
}