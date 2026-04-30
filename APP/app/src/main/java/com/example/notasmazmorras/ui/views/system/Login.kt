package com.example.notasmazmorras.ui.views.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.viewmodels.CampaignViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    authenticated: Boolean,
    onSuccess: () -> Unit,
    onLog: (String, String) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        }
    ){ contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = {navController.navigate("createAccount")}
            ) {
                Text("Crear cuenta")
            }

            LoginScreen(
                authenticated = authenticated,
                onSuccess = onSuccess,
                onLog = onLog,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun LoginScreen(
    authenticated: Boolean,
    onSuccess: () -> Unit,
    onLog: (String, String) -> Unit,
    modifier: Modifier
){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (authenticated) { onSuccess() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        TextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
        )

        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { onLog(email, password) }
        ) {
            Text("Loggearse")
        }

    }

}