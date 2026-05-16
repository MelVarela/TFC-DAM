package com.example.notasmazmorras.ui.views.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.ui.components.InvitationCard
import com.example.notasmazmorras.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Invitations(
    invitations: List<LocalUserRelation>,
    onAccepted: (LocalUserRelation) -> Unit,
    onRejected: (LocalUserRelation) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.invitations)) },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
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
        }
    }
}