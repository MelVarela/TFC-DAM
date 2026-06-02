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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.viewmodels.UploadState
import androidx.core.net.toUri
import com.example.notasmazmorras.R
import com.example.notasmazmorras.data.model.remote.DndClass
import com.example.notasmazmorras.ui.components.NavigationMenu
import com.example.notasmazmorras.ui.views.system.HomeScreen

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
    NavigationMenu(
        mostrarMenu = false,
        goBack = true,
        onBack = {navController.popBackStack()},
        navController = navController,
        floatingAction = false,
        onFloating = {}
    ) {
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

    var fotoNull : String = "http://10.0.2.2:8080/api/v1/images/1225.png"

    var blankName by remember { mutableStateOf(false) }

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
            text = stringResource(R.string.edit_character),
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

        Column {

            Button(
                onClick = {desplegado = !desplegado},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {Text(clase)}

            DropdownMenu(
                expanded = desplegado,
                onDismissRequest = {},
                modifier = Modifier.fillMaxWidth()
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

        }

        Column {

            Button(
                onClick = {desplegadoSubclase = !desplegadoSubclase},
                colors = ButtonDefaults.buttonColors(containerColor = Color(77, 126, 153, 255)),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {Text(subclase)}

            DropdownMenu(
                expanded = desplegadoSubclase,
                onDismissRequest = {},
                modifier = Modifier.fillMaxWidth()
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

        }

        TextField(
            value = maxPg.toString(),
            onValueChange = {maxPg = Integer.parseInt(if(it != "") it else "0")},
            label = { Text(stringResource(R.string.max_pg)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.padding(8.dp)
        )

        TextField(
            value = pg.toString(),
            onValueChange = {pg = Integer.parseInt(if(it != "") it else "0")},
            label = { Text(stringResource(R.string.pg)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.padding(8.dp)
        )

        if(blankName){
            Text(
                text = stringResource(R.string.blank_name),
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
                if(name.isNotEmpty()){
                    blankName = false
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
                }else{
                    blankName = true
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