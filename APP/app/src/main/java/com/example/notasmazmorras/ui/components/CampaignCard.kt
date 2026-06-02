package com.example.notasmazmorras.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.example.notasmazmorras.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
        onClick = { onSelect(campaign.id) },
        colors = CardDefaults.cardColors(
            containerColor = Color(229, 246, 255, 255)
        )
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

                Text(
                    text = campaign.name,
                    softWrap = true,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        horizontal = 4.dp
                    ).weight(1f)
                )

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