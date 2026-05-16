package com.example.notasmazmorras.ui.views.campaign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.example.notasmazmorras.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    onChangeSchedule: () -> Unit,
    listSchedules: List<String>,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.calendar)) },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.go_back))
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
            CalendarioScreen(
                listSchedules = listSchedules
            )
            Button(
                onClick = {onChangeSchedule()}
            ) {
                Text(stringResource(R.string.change_schedule))
            }
        }
    }
}

@Composable
fun CalendarioScreen(
    listSchedules: List<String>
){

    var lunes = true
    var martes = true
    var miercoles = true
    var jueves = true
    var viernes = true
    var sabado = true
    var domingo = true

    listSchedules.map {
        if(!it.contains("lunes")){
            lunes = false
        }
        if(!it.contains("martes")){
            martes = false
        }
        if(!it.contains("miercoles")){
            miercoles = false
        }
        if(!it.contains("jueves")){
            jueves = false
        }
        if(!it.contains("viernes")){
            viernes = false
        }
        if(!it.contains("sabado")){
            sabado = false
        }
        if(!it.contains("domingo")){
            domingo = false
        }
    }

    Row {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(CornerSize(8.dp)),
                    color = if(lunes) colorScheme.secondaryContainer else Color.Red,
                )
        ){
            Text(modifier = Modifier.padding(8.dp), text = stringResource(R.string.ltr_monday))
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(CornerSize(8.dp)),
                    color = if(martes) colorScheme.secondaryContainer else Color.Red,
                )
        ){
            Text(modifier = Modifier.padding(8.dp), text = stringResource(R.string.ltr_tuesday))
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(CornerSize(8.dp)),
                    color = if(miercoles) colorScheme.secondaryContainer else Color.Red,
                )
        ){
            Text(modifier = Modifier.padding(8.dp), text = stringResource(R.string.ltr_wednesday))
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(CornerSize(8.dp)),
                    color = if(jueves) colorScheme.secondaryContainer else Color.Red,
                )
        ){
            Text(modifier = Modifier.padding(8.dp), text = stringResource(R.string.ltr_thursday))
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(CornerSize(8.dp)),
                    color = if(viernes) colorScheme.secondaryContainer else Color.Red,
                )
        ){
            Text(modifier = Modifier.padding(8.dp), text = stringResource(R.string.ltr_friday))
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(CornerSize(8.dp)),
                    color = if(sabado) colorScheme.secondaryContainer else Color.Red,
                )
        ){
            Text(modifier = Modifier.padding(8.dp), text = stringResource(R.string.ltr_saturday))
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(CornerSize(8.dp)),
                    color = if(domingo) colorScheme.secondaryContainer else Color.Red,
                )
        ){
            Text(modifier = Modifier.padding(8.dp), text = stringResource(R.string.ltr_sunday))
        }
    }

}