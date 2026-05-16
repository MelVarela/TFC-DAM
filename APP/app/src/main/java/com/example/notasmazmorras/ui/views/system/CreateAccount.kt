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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.notasmazmorras.data.model.UserAccount
import com.example.notasmazmorras.R
import com.example.notasmazmorras.viewmodels.UploadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccount(
    onDone: (UserAccount) -> Unit,
    uploadImage: (Bitmap?) -> Unit,
    uploadState: UploadState,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_account)) },
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
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        TextField(
            value = userName,
            onValueChange = {userName = it},
            label = { Text(stringResource(R.string.user_name)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
        )

        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        TextField(
            value = email,
            onValueChange = {email = it},
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
        )

        Button(
            onClick = {
                imagePickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        ) {
            Text(stringResource(R.string.select_photo))
        }

        AsyncImage(
            model = selectedImageUri,
            contentDescription = ""
        )

        Button(
            onClick = {
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
        ) { Text(stringResource(R.string.create_account)) }

        if(uploadState.isLoading){
            Text(stringResource(R.string.loading))
        }

    }

}