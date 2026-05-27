package com.example.notasmazmorras.ui.views.system

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.notasmazmorras.data.model.UserAccount
import com.example.notasmazmorras.R
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.viewmodels.UploadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccount(
    onDone: (UserAccount) -> Unit,
    uploadImage: (Bitmap?) -> Unit,
    uploadState: UploadState,
    navController: NavController
) {
    NavigationMenu(
        mostrarMenu = false,
        goBack = true,
        onBack = {navController.popBackStack()},
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CreateAccountScreen(
                onDone = onDone,
                uploadImage = uploadImage,
                uploadState = uploadState,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun CreateAccountScreen(
    onDone: (UserAccount) -> Unit,
    uploadImage: (Bitmap?) -> Unit,
    uploadState: UploadState,
    modifier : Modifier,
){

    val ctx = LocalContext.current.contentResolver

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var fotoActual by remember { mutableStateOf<Bitmap?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it

            if (Build.VERSION.SDK_INT < 28) {
                fotoActual =
                    MediaStore.Images.Media.getBitmap(ctx, it)
            } else {
                if(it != null){
                    val source = ImageDecoder.createSource(ctx, it)
                    fotoActual = ImageDecoder.decodeBitmap(source)
                }
            }
        }
    )

    if(!uploadState.isLoading && uploadState.uploadStarted){
        if (uploadState.error != null){
            Log.d("ERR", "Error subiendo una foto. ${uploadState.error}")
        }

        onDone(UserAccount(
            email = email,
            password = password,
            name = userName,
            profilePicture = uploadState.url
        ))
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 64.dp)
            .background(Color(229, 246, 255, 255))
            .border(
                width = 1.dp,
                color = Color(5, 35, 51, 255),
                shape = RoundedCornerShape(4)
            ),
    ){

        Text(
            text = stringResource(R.string.create_account),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier.padding(12.dp)
        )

        TextField(
            value = userName,
            onValueChange = {userName = it},
            label = { Text(stringResource(R.string.user_name)) },
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
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.padding(8.dp)
        )

        TextField(
            value = passwordConfirm,
            onValueChange = {passwordConfirm = it},
            label = { Text(stringResource(R.string.password_confirm)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.padding(8.dp)
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

        Button(
            onClick = {
                imagePickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.select_photo))
        }

        AsyncImage(
            model = selectedImageUri,
            contentDescription = ""
        )

        Button(
            onClick = {
                if(password.equals(passwordConfirm)){
                    if(fotoActual != null){
                        uploadImage(fotoActual)
                    }else{
                        onDone(UserAccount(
                            email = email,
                            password = password,
                            name = userName,
                            profilePicture = ""
                        ))
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) { Text(stringResource(R.string.create_account)) }

        if(uploadState.isLoading){
            Text(stringResource(R.string.loading))
        }

    }

}