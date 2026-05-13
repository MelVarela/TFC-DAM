package com.example.notasmazmorras.ui.views.campaign.details

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.viewmodels.UploadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCreature(
    onDone: (LocalCreature) -> Unit,
    uploadImage: (Bitmap?) -> Unit,
    uploadState: UploadState,
    creatures: List<LocalCreature>,
    creatureId: String?,
    campaign: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Creature") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            EditCreatureScreen(
                onDone = onDone,
                uploadImage = uploadImage,
                uploadState = uploadState,
                creatures = creatures,
                creatureId = creatureId,
                campaign = campaign,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun EditCreatureScreen(
    onDone: (LocalCreature) -> Unit,
    uploadImage: (Bitmap?) -> Unit,
    uploadState: UploadState,
    creatures: List<LocalCreature>,
    creatureId: String?,
    campaign: String,
    modifier: Modifier
){

    val ctx = LocalContext.current.contentResolver

    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var fotoActual by remember { mutableStateOf<Bitmap?>(null) }
    var fotoCambiada by remember { mutableStateOf(false) }

    var fotoNull : String = "https://deltarune.com/assets/images/ie-info.png"

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
            fotoCambiada = true

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

    if(creatureId != null){
        val creature : LocalCreature = creatures.first{it.id == creatureId}
        name = creature.name
        species = creature.species

        fotoNull = creature.picture
        selectedImageUri = creature.picture.toUri()
    }

    if(!uploadState.isLoading && uploadState.uploadStarted){
        if (uploadState.error != null){
            Log.d("ERR", "Error subiendo una foto. ${uploadState.error}")
        }

        if(creatureId != null){
            onDone(LocalCreature(
                creatureId,
                name,
                species,
                uploadState.url,
                campaign,
                true
            ))
        }else{
            onDone(LocalCreature(
                "local_${System.nanoTime()}crea",
                name,
                species,
                uploadState.url,
                campaign,
                true
            ))
        }
    }

    Scaffold { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){

            TextField(
                value = name,
                onValueChange = {name = it},
                label = { Text("Nombre") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
            )

            TextField(
                value = species,
                onValueChange = {species = it},
                label = { Text("Especie") },
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
                Text("Selecciona una foto")
            }

            AsyncImage(
                model = selectedImageUri,
                contentDescription = ""
            )

            Button(
                onClick = {
                    if(!fotoCambiada || fotoActual == null){

                        if(creatureId != null){
                            onDone(LocalCreature(
                                creatureId,
                                name,
                                species,
                                fotoNull,
                                campaign,
                                true
                            ))
                        }else{
                            onDone(LocalCreature(
                                "local_${System.nanoTime()}crea",
                                name,
                                species,
                                fotoNull,
                                campaign,
                                true
                            ))
                        }

                    }else{
                        uploadImage(fotoActual)
                    }
                }
            ) { Text("Done") }

            if(uploadState.isLoading){
                Text("Cargando...")
            }
        }
    }
}