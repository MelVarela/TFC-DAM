package com.example.notasmazmorras.ui.views.campaign.details

import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.notasmazmorras.R
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.ui.views.system.HomeScreen
import com.example.notasmazmorras.viewmodels.UploadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditObject(
    onDone: (LocalObject) -> Unit,
    uploadImage: (Bitmap?, Context) -> Unit,
    uploadState: UploadState,
    objects: List<LocalObject>,
    objectId: String?,
    campaign: String,
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
        EditObjectScreen(
            onDone = onDone,
            uploadImage = uploadImage,
            uploadState = uploadState,
            objects = objects,
            objectId = objectId,
            campaign = campaign,
        )
    }
}

@Composable
fun EditObjectScreen(
    onDone: (LocalObject) -> Unit,
    uploadImage: (Bitmap?, Context) -> Unit,
    uploadState: UploadState,
    objects: List<LocalObject>,
    objectId: String?,
    campaign: String,
){

    val ctx = LocalContext.current

    var name by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("0") }
    var loadData by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var fotoActual by remember { mutableStateOf<Bitmap?>(null) }
    var fotoCambiada by remember { mutableStateOf(false) }

    var fotoNull : String = "http://10.0.2.2:8080/api/v1/images/1225.png"

    var blankName by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
            fotoCambiada = true

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

    if(objectId != null && !loadData){
        val obxecto : LocalObject = objects.first{it.id == objectId}
        name = obxecto.name
        cost = obxecto.cost.toString()

        fotoNull = obxecto.picture
        selectedImageUri = obxecto.picture.toUri()
    }

    if(!uploadState.isLoading && uploadState.uploadStarted){
        if (uploadState.error != null){
            Log.d("ERR", "Error subiendo una foto. ${uploadState.error}")
        }

        if(objectId != null){
            onDone(LocalObject(
                objectId,
                name,
                cost.toFloat(),
                uploadState.url,
                campaign,
                true
            ))
        }else{
            onDone(LocalObject(
                "local_${System.nanoTime()}obje",
                name,
                cost.toFloat(),
                uploadState.url,
                campaign,
                true
            ))
        }
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
            text = stringResource(R.string.edit_object),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier.padding(12.dp)
        )

        TextField(
            value = name,
            onValueChange = {name = it},
            label = { Text(stringResource(R.string.name)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.padding(8.dp)
        )

        TextField(
            value = cost,
            onValueChange = {cost = it},
            label = { Text(stringResource(R.string.price)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

        if(blankName){
            Text(
                text = stringResource(R.string.blank_name),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }

        Button(onClick = {
            try{
                if(name.isNotEmpty()){
                    blankName = false
                    if(!fotoCambiada || fotoActual == null){

                        if(objectId != null){
                            onDone(LocalObject(
                                objectId,
                                name,
                                cost.toFloat(),
                                fotoNull,
                                campaign,
                                true
                            ))
                        }else{
                            onDone(LocalObject(
                                "local_${System.nanoTime()}obje",
                                name,
                                cost.toFloat(),
                                fotoNull,
                                campaign,
                                true
                            ))
                        }

                    }else{
                        uploadImage(fotoActual, ctx)
                    }
                }else{
                    blankName = true
                }
            }catch (e: Throwable){
                Log.d("ERR", "${e.message}")
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
        ) { Text(stringResource(R.string.done)) }

        if(uploadState.isLoading){
            Text(stringResource(R.string.loading))
        }
    }
}