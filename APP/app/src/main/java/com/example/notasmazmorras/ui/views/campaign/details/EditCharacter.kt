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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.viewmodels.UploadState
import androidx.core.net.toUri
import com.example.notasmazmorras.R
import com.example.notasmazmorras.data.model.remote.DndClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCharacter(
    onDone: (LocalCharacter) -> Unit,
    uploadImage: (Bitmap?) -> Unit,
    uploadState: UploadState,
    characters: List<LocalCharacter>,
    characterId: String?,
    campaign: String,
    clases: List<DndClass>,
    subClases: List<String>,
    onClasSelected: (String) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_character)) },
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
            EditCharacterScreen(
                onDone = onDone,
                uploadImage = uploadImage,
                uploadState = uploadState,
                characterId = characterId,
                characters = characters,
                campaign = campaign,
                clases = clases,
                subClases = subClases,
                onClasSelected = onClasSelected,
                modifier = Modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCharacterScreen(
    onDone: (LocalCharacter) -> Unit,
    uploadImage: (Bitmap?) -> Unit,
    uploadState: UploadState,
    characters: List<LocalCharacter>,
    characterId : String?,
    campaign: String,
    clases: List<DndClass>,
    subClases: List<String>,
    onClasSelected: (String) -> Unit,
    modifier: Modifier
){

    val context = LocalContext.current
    val ctx = context.contentResolver

    var name by remember { mutableStateOf("") }
    var clase by remember { mutableStateOf(context.getString(R.string.char_sel_class)) }
    var subclase by remember { mutableStateOf(context.getString(R.string.char_sel_subclass)) }
    var maxPg by remember { mutableIntStateOf(0) }
    var pg by remember { mutableIntStateOf(0) }

    var desplegado by remember { mutableStateOf(false) }
    var desplegadoSubclase by remember { mutableStateOf(false) }

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

    if(characterId != null){
        val character : LocalCharacter = characters.first {it.id == characterId}
        name = character.name
        clase = character.clase
        subclase = character.subClase
        maxPg = character.maxPg
        pg = character.pg

        fotoNull = character.picture
        selectedImageUri = character.picture.toUri()
    }

    if(!uploadState.isLoading && uploadState.uploadStarted){
        if (uploadState.error != null){
            Log.d("ERR", "Error subiendo una foto. ${uploadState.error}")
        }

        if(characterId != null){
            onDone(LocalCharacter(
                characterId,
                name,
                clase,
                subclase,
                maxPg,
                pg,
                uploadState.url,
                campaign,
                true
            ))
        }else{
            onDone(LocalCharacter(
                "local_${System.nanoTime()}char",
                name,
                clase,
                subclase,
                maxPg,
                pg,
                uploadState.url,
                campaign,
                true
            ))
        }
    }

    Scaffold(
    ){ contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){

            TextField(
                value = name,
                onValueChange = {name = it},
                label = { Text(stringResource(R.string.name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
            )

            Button(
                onClick = {desplegado = !desplegado}
            ) {Text(clase)}
            DropdownMenu(
                expanded = desplegado,
                onDismissRequest = {}
            ) {

                clases.forEach { claseI ->
                    DropdownMenuItem(
                        text = {Text(claseI.name)},
                        onClick = {
                            onClasSelected(claseI.index)
                            clase = claseI.name
                            desplegado = false
                        }
                    )
                }

            }

            Button(
                onClick = {desplegadoSubclase = !desplegadoSubclase}
            ) {Text(subclase)}
            DropdownMenu(
                expanded = desplegadoSubclase,
                onDismissRequest = {}
            ) {

                if(subClases.isNotEmpty()){
                    subClases.forEach { subI ->
                        DropdownMenuItem(
                            text = {Text(subI)},
                            onClick = {
                                subclase = subI
                                desplegadoSubclase = false
                            }
                        )
                    }
                }else{
                    DropdownMenuItem(
                        text = {Text(stringResource(R.string.no_subclases))},
                        onClick = {
                            subclase = context.getString(R.string.no_subclases)
                            desplegadoSubclase = false
                        }
                    )
                }

            }

            TextField(
                value = maxPg.toString(),
                onValueChange = {maxPg = Integer.parseInt(if(it != "") it else "0")},
                label = { Text(stringResource(R.string.max_pg)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )

            TextField(
                value = pg.toString(),
                onValueChange = {pg = Integer.parseInt(if(it != "") it else "0")},
                label = { Text(stringResource(R.string.pg)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    if(!fotoCambiada || fotoActual == null){

                        if(characterId != null){
                            onDone(LocalCharacter(
                                characterId,
                                name,
                                clase,
                                subclase,
                                maxPg,
                                pg,
                                fotoNull,
                                campaign,
                                true
                            ))
                        }else{
                            onDone(LocalCharacter(
                                "local_${System.nanoTime()}char",
                                name,
                                clase,
                                subclase,
                                maxPg,
                                pg,
                                fotoNull,
                                campaign,
                                true
                            ))
                        }

                    }else{
                        uploadImage(fotoActual)
                    }
                }
            ) { Text(stringResource(R.string.done)) }

            if(uploadState.isLoading){
                Text(stringResource(R.string.loading))
            }

        }
    }
}