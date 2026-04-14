package com.example.notasmazmorras.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.notasmazmorras.data.model.local.LocalCharacter

@Composable
fun CharacterCard(
    character: LocalCharacter,
    onDelete: (LocalCharacter) -> Unit,
    onSelect: (String) -> Unit,
    onEdit: (String) -> Unit,
    modifier: Modifier = Modifier
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = CardDefaults.elevatedShape
    ) {
        Column (
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
        ){
            Button(
                onClick = { onSelect(character.id) },
                colors = ButtonColors(
                    containerColor = Color(0f, 0f, 0f, 0f),
                    disabledContainerColor = Color(0f, 0f, 0f, 0f),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray,
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(character.picture)
                            .crossfade(true)
                            .build(),
                        contentDescription = character.name,
                        //error = painterResource(id = R.drawable.screen_background_dark),
                        //placeholder = painterResource(id = R.drawable.screen_background_light),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                    )

                    Text(character.name)

                    Box(
                        modifier = Modifier.width(120.dp)
                            .padding(8.dp)
                    ){
                        LinearProgressIndicator(
                            progress = { (character.pg.toFloat() / character.maxPg.toFloat()) },
                            color = Color.Red,
                            trackColor = Color.Black,
                            gapSize = 0.dp
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.End ,
                        modifier = Modifier.weight(0.2f)
                    ) {
                        IconButton(onClick = { onEdit(character.id) }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { onDelete(character) }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }
}