package com.example.notasmazmorras.ui.views.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.notasmazmorras.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeSchedule(
    onDone: (String) -> Unit,
    schedule: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.change_schedule_title)) },
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
            ChangeScheduleScreen(
                onDone = onDone,
                schedule = schedule
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeScheduleScreen(
    onDone: (String) -> Unit,
    schedule: String
){

    var lunes by remember { mutableStateOf(schedule.contains("lunes")) }
    var martes by remember { mutableStateOf(schedule.contains("martes")) }
    var miercoles by remember { mutableStateOf(schedule.contains("miercoles")) }
    var jueves by remember { mutableStateOf(schedule.contains("jueves")) }
    var viernes by remember { mutableStateOf(schedule.contains("viernes")) }
    var sabado by remember { mutableStateOf(schedule.contains("sabado")) }
    var domingo by remember { mutableStateOf(schedule.contains("domingo")) }

    Column {
        Text(stringResource(R.string.days_available))
        Row {
            Column {
                Checkbox(
                    checked = lunes,
                    onCheckedChange = { lunes = it }
                )
                Text(stringResource(R.string.monday))
            }

            Column {
                Checkbox(
                    checked = martes,
                    onCheckedChange = { martes = it }
                )
                Text(stringResource(R.string.tuesday))
            }

            Column {
                Checkbox(
                    checked = miercoles,
                    onCheckedChange = { miercoles = it }
                )
                Text(stringResource(R.string.wednesaday))
            }

            Column {
                Checkbox(
                    checked = jueves,
                    onCheckedChange = { jueves = it }
                )
                Text(stringResource(R.string.thursday))
            }

        }
        Row {
            Column {
                Checkbox(
                    checked = viernes,
                    onCheckedChange = { viernes = it }
                )
                Text(stringResource(R.string.friday))
            }

            Column {
                Checkbox(
                    checked = sabado,
                    onCheckedChange = { sabado = it }
                )
                Text(stringResource(R.string.saturday))
            }

            Column {
                Checkbox(
                    checked = domingo,
                    onCheckedChange = { domingo = it }
                )
                Text(stringResource(R.string.friday))
            }
        }

        Button(
            onClick = {
                var str = ""

                if(lunes) str = str.plus("lunes/")
                if(martes) str = str.plus("martes/")
                if(miercoles) str = str.plus("miercoles/")
                if(jueves) str = str.plus("jueves/")
                if(viernes) str = str.plus("viernes/")
                if(sabado) str = str.plus("sabado/")
                if(domingo) str = str.plus("domingo/")

                onDone(str)
            }
        ) { Text(stringResource(R.string.done)) }
    }

}