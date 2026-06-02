package com.example.notasmazmorras.ui.views.campaign

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.R
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.ui.views.system.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitePlayer(
    onDone: (LocalUserRelation) -> Unit,
    campaign: String,
    navController: NavController
){

    NavigationMenu(
        mostrarMenu = false,
        goBack = true,
        onBack = {navController.popBackStack()},
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
        InvitePlayerScreen(
            onDone = onDone,
            campaign = campaign,
        )
    }

}

@Composable
fun InvitePlayerScreen(
    onDone: (LocalUserRelation) -> Unit,
    campaign: String,
){
    var player by remember { mutableStateOf("") }

    var blankPlayer by remember { mutableStateOf(false) }
    var badEmail by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp),
    ){
        TextField(
            value = player,
            onValueChange = {player = it},
            label = { Text(stringResource(R.string.email_to_invite)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.padding(8.dp)
        )

        if(blankPlayer){
            Text(
                text = stringResource(R.string.blank_invite),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }else if(badEmail){
            Text(
                text = stringResource(R.string.bad_email_format),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }

        Button(
            onClick = {
                if(player.isNotEmpty()){
                    blankPlayer = false
                    if(Patterns.EMAIL_ADDRESS.matcher(player).matches()){
                        badEmail = false
                        onDone(
                            LocalUserRelation(
                                isAccepted = false,
                                role = "p",
                                schedule = "",
                                user = player,
                                campaign = campaign
                            )
                        )
                    }else{
                        badEmail = true
                    }
                }else{
                    blankPlayer = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.invite_player))
        }
    }

}