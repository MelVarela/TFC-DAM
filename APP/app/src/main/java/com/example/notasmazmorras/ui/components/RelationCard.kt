package com.example.notasmazmorras.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notasmazmorras.R
import com.example.notasmazmorras.data.model.local.LocalUserRelation

@Composable
fun RelationCard(
    userRelation: LocalUserRelation,
    onDelete: (LocalUserRelation) -> Unit,
    onUserSelected: (String) -> Unit,
    modifier: Modifier
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = CardDefaults.elevatedShape,
        onClick = {onUserSelected(userRelation.user)},
        colors = CardDefaults.cardColors(
            containerColor = Color(229, 246, 255, 255)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ){

            Text(
                userRelation.user,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            if(userRelation.isAccepted){
                if(userRelation.role == "d"){
                    Text(stringResource(R.string.dm))
                }else{
                    Text(stringResource(R.string.player))
                }
            }else{
                Text(stringResource(R.string.pending))
            }

            if(userRelation.role != "d"){
                IconButton(onClick = { onDelete(userRelation) }) {
                    Icon(Icons.Outlined.Delete, contentDescription = stringResource(R.string.delete))
                }
            }
        }
    }

}