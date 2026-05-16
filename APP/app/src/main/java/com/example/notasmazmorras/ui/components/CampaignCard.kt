package com.example.notasmazmorras.ui.components

import com.example.notasmazmorras.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.notasmazmorras.data.model.local.LocalCampaign

@Composable
fun CampaignCard(
    campaign: LocalCampaign,
    onDelete: (LocalCampaign) -> Unit,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = CardDefaults.elevatedShape,
        onClick = { onSelect(campaign.id) }
    ) {
        Column (
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start,
        ){
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(campaign.picture)
                        .crossfade(true)
                        .build(),
                    contentDescription = campaign.name,
                    //error = painterResource(id = R.drawable.screen_background_dark),
                    //placeholder = painterResource(id = R.drawable.screen_background_light),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                )

                Text(campaign.name)

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.End ,
                    modifier = Modifier.weight(0.2f)
                ) {
                    IconButton(onClick = { onDelete(campaign) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = stringResource(R.string.delete))
                    }
                }

            }
        }
    }

}