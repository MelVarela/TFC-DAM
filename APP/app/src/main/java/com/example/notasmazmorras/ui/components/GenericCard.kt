package com.example.notasmazmorras.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun GenericCard(
    onDelete: () -> Unit,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    picture: String,
    name: String,
    modifier: Modifier = Modifier
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = CardDefaults.elevatedShape
    ){
        Column (
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
        ){
            Button(
                onClick = { onSelect() },
                colors = ButtonColors(
                    containerColor = Color(0f, 0f, 0f, 0f),
                    disabledContainerColor = Color(0f, 0f, 0f, 0f),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray,
                )
            ){
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(picture)
                            .crossfade(true)
                            .build(),
                        contentDescription = name,
                        //error = painterResource(id = R.drawable.screen_background_dark),
                        //placeholder = painterResource(id = R.drawable.screen_background_light),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                    )

                    Text(name)

                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.End ,
                        modifier = Modifier.weight(0.2f)
                    ) {
                        IconButton(onClick = { onEdit() }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { onDelete() }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }

}