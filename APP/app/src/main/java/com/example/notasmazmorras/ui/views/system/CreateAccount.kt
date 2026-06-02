package com.example.notasmazmorras.ui.views.system

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.notasmazmorras.data.model.UserAccount
import com.example.notasmazmorras.R
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.viewmodels.CreateState
import com.example.notasmazmorras.viewmodels.UploadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccount(
    onDone: (UserAccount, Context) -> Unit,
    onSucces: (UserAccount) -> Unit,
    uploadImage: (Bitmap?, Context) -> Unit,
    uploadState: UploadState,
    createStatus: CreateState,
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
                onSucces = onSucces,
                uploadImage = uploadImage,
                uploadState = uploadState,
                createStatus = createStatus
            )
        }
    }
}

@Composable
fun CreateAccountScreen(
    onDone: (UserAccount, Context) -> Unit,
    onSucces: (UserAccount) -> Unit,
    uploadImage: (Bitmap?, Context) -> Unit,
    uploadState: UploadState,
    createStatus: CreateState,
){

    val ctx = LocalContext.current

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var fotoActual by remember { mutableStateOf<Bitmap?>(null) }

    var badPassword by remember { mutableStateOf(false) }
    var badEmail by remember { mutableStateOf(false) }
    var blankPassword by remember { mutableStateOf(false) }
    var blankUser by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it

            if (Build.VERSION.SDK_INT < 28) {
                fotoActual =
                    MediaStore.Images.Media.getBitmap(ctx.contentResolver, it)
            } else {
                if(it != null){
                    val source = ImageDecoder.createSource(ctx.contentResolver, it)
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
        ), ctx)
    }

    if(createStatus.created){
        onSucces(UserAccount(
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

        if(badEmail){
            Text(
                text = stringResource(R.string.bad_email_format),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }else if(blankUser){
            Text(
                text = stringResource(R.string.blank_user),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }else if(blankPassword){
            Text(
                text = stringResource(R.string.blank_password),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }else if(badPassword){
            Text(
                text = stringResource(R.string.bad_password),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }

        if(createStatus.badEmail){
            Text(
                text = stringResource(R.string.bad_email),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }

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
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    badEmail = false
                    if(password.isNotEmpty()){
                        blankPassword = false
                        if(userName.isNotEmpty()){
                            blankUser = false
                            if(password == passwordConfirm){
                                badPassword = false
                                if(fotoActual != null){
                                    uploadImage(fotoActual, ctx)
                                }else{
                                    onDone(UserAccount(
                                        email = email,
                                        password = password,
                                        name = userName,
                                        profilePicture = ""
                                    ), ctx)
                                }
                            }else{
                                badPassword = true
                            }
                        }else{
                            blankUser = true
                        }
                    }else{
                        blankPassword = true
                    }
                }else{
                    badEmail = true
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