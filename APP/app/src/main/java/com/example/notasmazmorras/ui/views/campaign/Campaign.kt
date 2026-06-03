package com.example.notasmazmorras.ui.views.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.R
import com.example.notasmazmorras.ui.components.NavigationMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Campaign(
    campaign: LocalCampaign?,
    onSync: () -> Unit,
    onUserRelations: (String) -> Unit,
    navController: NavController
) {

    if(campaign == null) {
        navController.popBackStack()
    }

    NavigationMenu(
        mostrarMenu = true,
        goBack = false,
        onBack = {},
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
        CampaignScreen(
            campaign = campaign,
            onSync = onSync,
            onUserRelations = onUserRelations,
            navController = navController,
        )
    }
}

@Composable
fun CampaignScreen(
    campaign: LocalCampaign?,
    onSync: () -> Unit,
    onUserRelations: (String) -> Unit,
    navController: NavController
){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ){

        Text(
            text = campaign?.name ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier.padding(12.dp)
        )

        Row(

        ){

            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/calendar")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .padding(8.dp)
                    .height(128.dp)
                    .width(128.dp)
            ) {
                Text(stringResource(R.string.calendar))
            }
            Button(
                onClick = {onUserRelations("campaign/${campaign!!.id}/players")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .padding(8.dp)
                    .height(128.dp)
                    .width(128.dp)
            ) {
                Text(stringResource(R.string.players))
            }

        }

        Row(

        ){

            Button(
                onClick = {navController.navigate("notes/${campaign!!.id}/notes")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .padding(8.dp)
                    .height(128.dp)
                    .width(128.dp)
            ) {
                Text(stringResource(R.string.notes))
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/characters")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .padding(8.dp)
                    .height(128.dp)
                    .width(128.dp)
            ) {
                Text(stringResource(R.string.characters))
            }

        }

        Row(

        ){

            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/objects")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .padding(8.dp)
                    .height(128.dp)
                    .width(128.dp)
            ) {
                Text(stringResource(R.string.objects))
            }
            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/creatures")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .padding(8.dp)
                    .height(128.dp)
                    .width(128.dp)
            ) {
                Text(stringResource(R.string.creatures))
            }

        }

        Row(

        ){

            Button(
                onClick = {navController.navigate("campaign/${campaign!!.id}/map")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .padding(8.dp)
                    .height(128.dp)
                    .width(128.dp)
            ) {
                Text(stringResource(R.string.map))
            }
            Button(
                onClick = { onSync() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .padding(8.dp)
                    .height(128.dp)
                    .width(128.dp)
            ) {
                Icon(Icons.Default.Sync, contentDescription = "Sync")
            }

        }

    }
}