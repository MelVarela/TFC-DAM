package com.example.notasmazmorras.ui.views.system

import android.accounts.Account
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.ui.components.NavigationMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Account(
    account: LocalUser?,
    navController: NavController
) {

    if(account == null) {
        navController.navigate("home")
    }else{
        NavigationMenu(
            mostrarMenu = true,
            goBack = false,
            onBack = {},
            navController = navController,
            floatingAction = false,
            onFloating = {}
        ) {
            AccountScreen(
                account = account
            )
        }
    }

}

@Composable
fun AccountScreen(
    account: LocalUser
){

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 64.dp)
            .fillMaxWidth()
    ){

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(account.profilePicture)
                .crossfade(true)
                .build(),
            contentDescription = account.email,
            //error = painterResource(id = R.drawable.screen_background_dark),
            //placeholder = painterResource(id = R.drawable.screen_background_light),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(end = 2.dp)
                .size(80.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = Color(5, 35, 51, 255),
                )
        )

        Text(
            text = account.name,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )

        Text(
            text = account.email,
            fontSize = 16.sp,
        )

    }

}