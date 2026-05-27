package com.example.notasmazmorras.ui.views.campaign.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.notasmazmorras.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.ui.views.system.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(
    caracteristicas: Map<String, String>,
    title: String,
    maxPg: Int?,
    pg: Int?,
    onAddObject: (String) -> Unit,
    charId: String,
    picture: String,
    onNotes: () -> Unit,
    onBack: () -> Unit,
    navController: NavController
) {

    NavigationMenu(
        mostrarMenu = false,
        goBack = true,
        onBack = {onBack()},
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
        DetailsScreen(
            caracteristicas = caracteristicas,
            title = title,
            maxPg = maxPg,
            pg = pg,
            onAddObject = onAddObject,
            charId = charId,
            picture = picture,
            onNotes = onNotes
        )
    }

}

@Composable
fun DetailsScreen(
    caracteristicas: Map<String, String>,
    title: String,
    maxPg: Int?,
    pg: Int?,
    onAddObject: (String) -> Unit,
    charId: String,
    picture: String,
    onNotes: () -> Unit
){

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    horizontal = 16.dp
                )
            )
    ){
        Button(
            onClick = { onNotes() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.see_notes))
        }

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(picture)
                .crossfade(true)
                .build(),
            contentDescription = title,
            //error = painterResource(id = R.drawable.screen_background_dark),
            //placeholder = painterResource(id = R.drawable.screen_background_light),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(end = 2.dp)
                .size(80.dp)
                .clip(CircleShape)
        )

        if(maxPg != null && pg != null){
            Box(
                modifier = Modifier.width(120.dp)
                    .padding(8.dp)
            ){
                LinearProgressIndicator(
                    progress = { (pg.toFloat() / maxPg.toFloat()) },
                    color = Color.Red,
                    trackColor = Color.Black,
                    gapSize = 0.dp
                )
            }

            Button(
                onClick = {
                    onAddObject(charId)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {Text(stringResource(R.string.add_object))}
        }

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            items(caracteristicas.toList()){ pair ->

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text = pair.first,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = pair.second)
                }

            }
        }
    }

}