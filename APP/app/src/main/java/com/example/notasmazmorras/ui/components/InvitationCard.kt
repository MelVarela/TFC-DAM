package com.example.notasmazmorras.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notasmazmorras.data.model.local.LocalUserRelation

@Composable
fun InvitationCard(
    userRelation: LocalUserRelation,
    onAccepted: (LocalUserRelation) -> Unit,
    onRejected: (LocalUserRelation) -> Unit,
    modifier: Modifier
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = CardDefaults.elevatedShape
    ) {
        Column {
            Text(userRelation.user)
            if(userRelation.isAccepted){
                if(userRelation.role == "D"){
                    Text("DM")
                }else{
                    Text("Jugador")
                }
            }else{
                Text("Pendiente")
            }
            IconButton(onClick = { onAccepted(userRelation) }) {
                Icon(Icons.Outlined.PersonAdd, contentDescription = "Aceptar")
            }
            IconButton(onClick = { onRejected(userRelation) }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Rechazar")
            }
        }
    }

}