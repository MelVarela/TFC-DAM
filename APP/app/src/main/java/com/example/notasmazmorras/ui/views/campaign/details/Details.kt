package com.example.notasmazmorras.ui.views.campaign.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(
    caracteristicas: Map<String, String>,
    title: String,
    maxPg: Int?,
    pg: Int?,
    picture: String,
    onNotes: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            Button(
                onClick = { onNotes() }
            ) {
                Text("Ver notas")
            }
            DetailsScreen(
                caracteristicas = caracteristicas,
                title = title,
                maxPg = maxPg,
                pg = pg,
                picture = picture
            )
        }
    }
}

@Composable
fun DetailsScreen(
    caracteristicas: Map<String, String>,
    title: String,
    maxPg: Int?,
    pg: Int?,
    picture: String
){

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = title)

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
        }

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            items(caracteristicas.toList()){ pair ->

                Text("${pair.first}: ${pair.second}")

            }
        }
    }

}