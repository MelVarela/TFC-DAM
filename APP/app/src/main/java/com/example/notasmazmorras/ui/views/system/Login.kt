package com.example.notasmazmorras.ui.views.system

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notasmazmorras.R
import com.example.notasmazmorras.ui.components.NavigationMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    authenticated: Boolean,
    onSuccess: (String) -> Unit,
    onCreate: () -> Unit,
    onLog: (String, String) -> Unit,
    navController: NavController
) {
    NavigationMenu(
        mostrarMenu = false,
        goBack = false,
        onBack = {},
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
        LoginScreen(
            authenticated = authenticated,
            onSuccess = onSuccess,
            onLog = onLog,
            onCreate = onCreate,
            modifier = Modifier
        )
    }
}

@Composable
fun LoginScreen(
    authenticated: Boolean,
    onSuccess: (String) -> Unit,
    onLog: (String, String) -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier
){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (authenticated) { onSuccess(email) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 64.dp)
            .background(Color(229, 246, 255, 255))
            .border(
                width = 1.dp,
                color = Color(5, 35, 51, 255),
                shape = RoundedCornerShape(4)
            )
    ){
        Text(
            text = stringResource(R.string.log_in),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier.padding(12.dp)
        )

        TextField(
            value = email,
            onValueChange = {email = it},
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.padding(8.dp)
        )

        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(8.dp)
        )

        Button(
            onClick = { onLog(email, password) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.login))
        }

        Button(
            onClick = {onCreate()},
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.create_account))
        }
    }

}