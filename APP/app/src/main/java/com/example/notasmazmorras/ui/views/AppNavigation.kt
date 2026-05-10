package com.example.notasmazmorras.ui.views

import android.util.Log
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.data.model.local.LocalNote
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.data.model.local.LocalPlace
import com.example.notasmazmorras.ui.views.campaign.Calendar
import com.example.notasmazmorras.ui.views.campaign.Campaign
import com.example.notasmazmorras.ui.views.campaign.ChangeSchedule
import com.example.notasmazmorras.ui.views.campaign.CreateCampaign
import com.example.notasmazmorras.ui.views.campaign.InvitePlayer
import com.example.notasmazmorras.ui.views.campaign.Note
import com.example.notasmazmorras.ui.views.campaign.Notes
import com.example.notasmazmorras.ui.views.campaign.Players
import com.example.notasmazmorras.ui.views.campaign.details.Characters
import com.example.notasmazmorras.ui.views.campaign.details.Creatures
import com.example.notasmazmorras.ui.views.campaign.details.Details
import com.example.notasmazmorras.ui.views.campaign.details.EditCharacter
import com.example.notasmazmorras.ui.views.campaign.details.EditCreature
import com.example.notasmazmorras.ui.views.campaign.details.EditMap
import com.example.notasmazmorras.ui.views.campaign.details.EditObject
import com.example.notasmazmorras.ui.views.campaign.details.MapComponent
import com.example.notasmazmorras.ui.views.campaign.details.Objects
import com.example.notasmazmorras.ui.views.system.Account
import com.example.notasmazmorras.ui.views.system.CreateAccount
import com.example.notasmazmorras.ui.views.system.Home
import com.example.notasmazmorras.ui.views.system.Invitations
import com.example.notasmazmorras.ui.views.system.Login
import com.example.notasmazmorras.ui.views.system.Options
import com.example.notasmazmorras.ui.views.system.ReportSuggest
import com.example.notasmazmorras.viewmodels.CampaignViewmodel
import com.example.notasmazmorras.viewmodels.CharacterViewmodel
import com.example.notasmazmorras.viewmodels.CreatureViewmodel
import com.example.notasmazmorras.viewmodels.NoteViewmodel
import com.example.notasmazmorras.viewmodels.ObjectViewmodel
import com.example.notasmazmorras.viewmodels.PlaceViewmodel
import com.example.notasmazmorras.viewmodels.SystemViewmodel
import com.example.notasmazmorras.viewmodels.UserViewmodel
import kotlinx.coroutines.awaitAll

@Composable
fun AppNavigation(

) {
    val navController = rememberNavController()

    val userViewmodel : UserViewmodel = viewModel(factory = UserViewmodel.Factory)
    val campaignViewmodel : CampaignViewmodel = viewModel(factory = CampaignViewmodel.Factory)
    val characterViewmodel : CharacterViewmodel = viewModel(factory = CharacterViewmodel.Factory)
    val creatureViewmodel : CreatureViewmodel = viewModel(factory = CreatureViewmodel.Factory)
    val noteViewmodel : NoteViewmodel = viewModel(factory = NoteViewmodel.Factory)
    val objectViewmodel : ObjectViewmodel = viewModel(factory = ObjectViewmodel.Factory)
    val placeViewmodel : PlaceViewmodel = viewModel(factory = PlaceViewmodel.Factory)
    val systemViewmodel : SystemViewmodel = viewModel(factory = SystemViewmodel.Factory)

    val userRelations by campaignViewmodel.userRelations.collectAsState()
    val campaigns by campaignViewmodel.campaigns.collectAsState()
    val characters by characterViewmodel.characters.collectAsState()
    val creatures by creatureViewmodel.creatures.collectAsState()
    val notes by noteViewmodel.notes.collectAsState()
    val objects by objectViewmodel.objects.collectAsState()
    val places by placeViewmodel.places.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){

        systemViewmodel.firstTimeOpened()
        systemViewmodel.checkIfStillAuthenticated()

        composable(route = "login"){
            Login(
                authenticated = (userViewmodel.authenticated.collectAsState().value || systemViewmodel.authenticated.collectAsState().value),
                onSuccess = { email ->
                    if(!systemViewmodel.authenticated.value) systemViewmodel.setLastSigned(email)
                    campaignViewmodel.sync(email)
                    campaignViewmodel.syncRelationsByUser(email)
                    navController.navigate("home")
                },
                onLog = {
                    email, password -> userViewmodel.login(email, password)
                },
                navController
            )
        }

        composable(route = "createAccount"){
            CreateAccount(
                onDone = {
                    userAccount ->
                    userViewmodel.insertUser(userAccount)
                    systemViewmodel.setLastSigned(userAccount.email)
                    navController.navigate("home")
                         },
                uploadImage = {
                        image -> if(image != null){
                    systemViewmodel.uploadImage(image)
                }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                navController
            )
        }

        composable(route = "home"){
            Home(
                campaigns = campaigns,
                onDelete = {
                    campaign -> campaignViewmodel.deleteCampaign(campaign)
                },
                onSelect = {
                    id ->
                        campaignViewmodel.setCurrentCampaign(id, systemViewmodel.currentUser.value)
                        navController.navigate("campaign/${id}")
                },
                onSync = {
                    campaignViewmodel.sync(systemViewmodel.currentUser.value)
                    for (campaign in campaigns) {
                        if(campaign.id.substring(0, 1) != "l"){
                            characterViewmodel.sync(campaign.id)
                            creatureViewmodel.sync(campaign.id)
                            noteViewmodel.sync(campaign.id)
                            objectViewmodel.sync(campaign.id)
                            placeViewmodel.sync(campaign.id)
                            campaignViewmodel.syncRelations(campaign.id)
                        }
                    }
                },
                onInvitations = {
                    campaignViewmodel.syncPending(systemViewmodel.currentUser.value)
                    navController.navigate("invitations")
                },
                name = systemViewmodel.currentUser.collectAsState().value,
                navController
            )
        }

        composable(route = "invitations"){
            Invitations(
                invitations = userRelations.filter { !it.isAccepted },
                onAccepted = {
                    userRelation -> campaignViewmodel.accept(userRelation)
                },
                onRejected = {
                        userRelation -> campaignViewmodel.reject(userRelation)
                },
                navController
            )
        }

        composable(
            route = "reportingSuggestions/{action}",
            arguments = listOf(navArgument("action"){type = NavType.StringType})
        ){ backStackEntry ->
            val action = backStackEntry.arguments?.getString("action")

            ReportSuggest(
                type = action!!,
                onDone = {
                    report -> systemViewmodel.sendReport(report)
                    navController.navigate("home")
                },
                navController
            )
        }

        composable(route = "options"){
            Options(
                onLogOut = {
                    systemViewmodel.logOut()
                    userViewmodel.logOut()

                    campaignViewmodel.logOut()
                    characterViewmodel.logOut()
                    creatureViewmodel.logOut()
                    noteViewmodel.logOut()
                    objectViewmodel.logOut()
                    placeViewmodel.logOut()

                    campaignViewmodel.setCurrentCampaign("", "")
                    navController.navigate("login")
                },
                navController
            )
        }

        composable(
            route = "account/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Account(navController)
        }

        composable(route = "createCampaign"){
            CreateCampaign(
                onDone = {
                    campaign ->

                        campaignViewmodel.insertCampaign(
                            campaign,
                            systemViewmodel.currentUser.value
                        )

                        systemViewmodel.finishUpload()

                        campaignViewmodel.createCampaign(campaign.id, systemViewmodel.currentUser.value)

                        campaignViewmodel.setCurrentCampaign(campaign.id, systemViewmodel.currentUser.value)
                        navController.navigate("campaign/" + campaign.id)
                 },
                uploadImage = {
                    image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                navController
            )
        }

        composable(
            route = "campaign/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            campaignViewmodel.setCurrentCampaign(id!!, systemViewmodel.currentUser.collectAsState().value)

            Campaign(
                campaign = campaigns.firstOrNull{ it.id == id },
                onBack = {navController.navigate("home")},
                onSync = {
                    campaignViewmodel.sync(id)
                    campaignViewmodel.syncRelations(id)
                    characterViewmodel.sync(id)
                    creatureViewmodel.sync(id)
                    noteViewmodel.sync(id)
                    objectViewmodel.sync(id)
                    placeViewmodel.sync(id)
                },
                onUserRelations = {
                    url ->
                        navController.navigate(url)
                },
                navController
            )
        }

        composable(
            route = "campaign/{id}/calendar",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){

            var schedules : List<String> = emptyList()
            userRelations.filter { it.campaign == campaignViewmodel.currentCampaign.value }.map {
                schedules = schedules.plus(it.schedule)
            }

            Calendar(
                onChangeSchedule = {
                    navController.navigate("campaign/${campaignViewmodel.currentCampaign.value}/changeSchedule")
                },
                listSchedules = schedules,
                navController
            )
        }

        composable(
            route = "campaign/{id}/changeSchedule",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")

            ChangeSchedule(
                onDone = { schedule ->
                    campaignViewmodel.updateSchedule(schedule, systemViewmodel.currentUser.value)
                    campaignViewmodel.syncRelations(campaignViewmodel.currentCampaign.value)
                    navController.navigate("campaign/${id}/calendar")
                },
                schedule = userRelations.firstOrNull {
                    it.campaign == campaignViewmodel.currentCampaign.value &&
                    it.user == systemViewmodel.currentUser.value
                                                     }?.schedule ?: "",
                navController
            )
        }

        composable(
            route = "campaign/{id}/players",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Players(
                userRelations = userRelations.filter { it.campaign == campaignViewmodel.currentCampaign.value },
                onDelete = {
                    relation -> campaignViewmodel.deleteRelation(relation)
                },
                onUserSelected = {
                    user -> navController.navigate("account/$user")
                },
                onBack = {
                    navController.navigate("campaign/${campaignViewmodel.currentCampaign.value}")
                },
                navController
            )
        }

        composable(
            route = "invitePlayer"
        ){
            InvitePlayer(
                onDone = { relation ->
                    campaignViewmodel.invitePlayer(relation)
                    navController.navigate("campaign/${campaignViewmodel.currentCampaign.value}")
                },
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "notes/{owner}/notes",
            arguments = listOf(
                navArgument("owner"){type = NavType.StringType}
            )
        ){ backStackEntry ->
            val ownerId = backStackEntry.arguments?.getString("owner")!!

            Notes(
                notes = notes.filter {
                    it.owner == ownerId &&
                        (
                            it.isDm == campaignViewmodel.isDm.value ||
                            !it.isDm
                        )
                                     },
                onPress = { id -> navController.navigate("note/${id}") },
                onDelete = { note -> noteViewmodel.deleteNote(note) },
                onNew = { navController.navigate("newNote/${ownerId}") },
                onSync = {
                    noteViewmodel.sync(ownerId)
                },
                onBack = {
                    try{
                        if(ownerId.subSequence((ownerId.length - 4), ownerId.length) == "camp"){
                            navController.navigate("campaign/${ownerId}")
                        }else if(ownerId.subSequence((ownerId.length - 4), ownerId.length) == "char"){
                            navController.navigate("details/char/${ownerId}")
                        }else if(ownerId.subSequence((ownerId.length - 4), ownerId.length) == "crea"){
                            navController.navigate("details/crea/${ownerId}")
                        }else if(ownerId.subSequence((ownerId.length - 4), ownerId.length) == "plac"){
                            navController.navigate("details/plac/${ownerId}")
                        }else if(ownerId.subSequence((ownerId.length - 4), ownerId.length) == "obje"){
                            navController.navigate("details/obje/${ownerId}")
                        }
                    }catch (e: Throwable){
                        Log.e("ERROR", e.message.toString())
                        navController.navigate("home")
                    }
                }
            )
        }

        composable(
            route = "note/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")

            val noteR : LocalNote = notes.first{ it.id == id }

            Note(
                note = noteR,
                onLoad = { noteViewmodel.setEditing(noteR, true) },
                onBack = {
                    note -> noteViewmodel.updateNote(note)
                    noteViewmodel.setEditing(note, false)
                    navController.navigate("notes/${note.owner}/notes")
                },
                owner = "",
                isDm = campaignViewmodel.isDm.collectAsState().value
            )
        }

        composable(
            route = "newNote/{ownerId}",
            arguments = listOf(navArgument("ownerId"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("ownerId")

            Note(
                note = null,
                onLoad = {},
                onBack = {
                    note -> noteViewmodel.insertNote(note)
                    navController.navigate("notes/${note.owner}/notes")
                },
                owner = id!!,
                isDm = campaignViewmodel.isDm.collectAsState().value
            )
        }

        composable(
            route = "campaign/{id}/characters",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val campId = backStackEntry.arguments?.getString("id")

            Characters(
                characters = characters.filter { it.campaign == campId },
                onDelete = {
                    character -> characterViewmodel.deleteCharacter(character)
                },
                onSelect = {
                    id -> navController.navigate("details/char/${id}")
                },
                onEdit = {
                    id -> navController.navigate("editCharacter/${id}")
                },
                onBack = { navController.navigate("campaign/${campId}") },
                onSync = {
                    characterViewmodel.sync(campaignViewmodel.currentCampaign.value)
                },
                navController
            )
        }

        composable(
            route = "campaign/{id}/objects",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val campId = backStackEntry.arguments?.getString("id")

            Objects(
                obxectos = objects.filter { it.campaign == campId },
                onDelete = {
                    obxecto -> objectViewmodel.deleteObject(obxecto)
                },
                onSelect = {
                    id -> navController.navigate("details/obje/${id}")
                },
                onEdit = {
                    id -> navController.navigate("editObject/${id}")
                },
                onBack = { navController.navigate("campaign/${campId}") },
                onSync = {
                    objectViewmodel.sync(campaignViewmodel.currentCampaign.value)
                },
                navController
            )
        }

        composable(
            route = "campaign/{id}/creatures",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val campId = backStackEntry.arguments?.getString("id")

            Creatures(
                creatures = creatures.filter { it.campaign == campId },
                onDelete = {
                    creature -> creatureViewmodel.deleteCreature(creature)
                },
                onSelect = {
                    id -> navController.navigate("details/crea/${id}")
                },
                onEdit = {
                    id -> navController.navigate("editCreature/${id}")
                },
                onBack = { navController.navigate("campaign/${campId}") },
                onSync = {
                    creatureViewmodel.sync(campaignViewmodel.currentCampaign.value)
                },
                navController
            )
        }

        composable(
            route = "campaign/{id}/map",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val campId = backStackEntry.arguments?.getString("id")

            MapComponent(
                places = places.filter { it.campaign == campId },
                onDelete = {
                    place -> placeViewmodel.deletePlace(place)
                },
                onSelect = {
                    id -> navController.navigate("details/plac/${id}")
                },
                onEdit = {
                    id -> navController.navigate("editMap/${id}")
                },
                onBack = { navController.navigate("campaign/${campId}") },
                onSync = {
                    placeViewmodel.sync(campaignViewmodel.currentCampaign.value)
                },
                navController
            )
        }

        composable(
            route = "editCharacter/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            EditCharacter(
                onDone = {
                    character -> characterViewmodel.updateCharacter(character)
                    systemViewmodel.finishUpload()
                    navController.navigate("campaign/${character.campaign}/characters")
                },
                uploadImage = {
                        image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                characters = characters,
                characterId = id,
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "editCharacter"
        ){
            EditCharacter(
                onDone = {
                    character -> characterViewmodel.insertCharacter(character)
                    systemViewmodel.finishUpload()
                    navController.navigate("campaign/${character.campaign}/characters")
                },
                uploadImage = {
                        image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                characters = characters,
                characterId = null,
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "editObject/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            EditObject(
                onDone = {
                    obxecto -> objectViewmodel.updateObject(obxecto)
                    systemViewmodel.finishUpload()
                    navController.navigate("campaign/${obxecto.campaign}/objects")
                },
                uploadImage = {
                        image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                objects = objects,
                objectId = id,
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "editObject"
        ){
            EditObject(
                onDone = {
                    obxecto -> objectViewmodel.insertObject(obxecto)
                    systemViewmodel.finishUpload()
                    navController.navigate("campaign/${obxecto.campaign}/objects")
                },
                uploadImage = {
                        image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                objects = objects,
                objectId = null,
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "editCreature/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            EditCreature(
                onDone = {
                    creature -> creatureViewmodel.updateCreature(creature)
                    systemViewmodel.finishUpload()
                    navController.navigate("campaign/${creature.campaign}/creatures")
                },
                uploadImage = {
                        image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                creatures = creatures,
                creatureId = id,
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "editCreature"
        ){
            EditCreature(
                onDone = {
                    creature -> creatureViewmodel.insertCreature(creature)
                    systemViewmodel.finishUpload()
                    navController.navigate("campaign/${creature.campaign}/creatures")
                },
                uploadImage = {
                        image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                creatures = creatures,
                creatureId = null,
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "editMap/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            EditMap(
                onDone = {
                    place -> placeViewmodel.updatePlace(place)
                    systemViewmodel.finishUpload()
                    navController.navigate("campaign/${place.campaign}/map")
                },
                uploadImage = {
                        image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                places = places,
                placeId = id,
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "editMap"
        ){
            EditMap(
                onDone = {
                    place -> placeViewmodel.insertPlace(place)
                    systemViewmodel.finishUpload()
                    navController.navigate("campaign/${place.campaign}/map")
                },
                uploadImage = {
                        image -> if(image != null){
                        systemViewmodel.uploadImage(image)
                    }
                },
                uploadState = systemViewmodel.uploadState.collectAsState().value,
                places = places,
                placeId = null,
                campaign = campaignViewmodel.currentCampaign.collectAsState().value,
                navController
            )
        }

        composable(
            route = "details/char/{detailId}",
            arguments = listOf(
                navArgument("detailId"){type = NavType.StringType}
            )
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("detailId")
            val chara : LocalCharacter = characters.first { it.id == id }

            val map = HashMap<String, String>()

            map.put("Clase", chara.clase)
            map.put("Subclase", chara.subClase)

            Details(
                caracteristicas = map.toMap(),
                title = chara.name,
                maxPg = chara.maxPg,
                pg = chara.pg,
                picture = chara.picture,
                onNotes = { navController.navigate("notes/${chara.id}/notes") },
                onBack = { navController.navigate("campaign/${chara.campaign}/characters") }
            )
        }

        composable(
            route = "details/crea/{detailId}",
            arguments = listOf(
                navArgument("detailId"){type = NavType.StringType}
            )
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("detailId")
            val creature : LocalCreature = creatures.first{ it.id == id }

            val map = HashMap<String, String>()

            map.put("Especie", creature.species)

            Details(
                caracteristicas = map.toMap(),
                title = creature.name,
                maxPg = null,
                pg = null,
                picture = creature.picture,
                onNotes = { navController.navigate("notes/${creature.id}/notes") },
                onBack = { navController.navigate("campaign/${creature.campaign}/creatures") }
            )
        }

        composable(
            route = "details/obje/{detailId}",
            arguments = listOf(
                navArgument("detailId"){type = NavType.StringType}
            )
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("detailId")
            val obxecto : LocalObject = objects.first { it.id == id }

            val map = HashMap<String, String>()

            map.put("Precio", obxecto.cost.toString())

            Details(
                caracteristicas = map.toMap(),
                title = obxecto.name,
                maxPg = null,
                pg = null,
                picture = obxecto.picture,
                onNotes = { navController.navigate("notes/${obxecto.id}/notes") },
                onBack = { navController.navigate("campaign/${obxecto.campaign}/objects") }
            )
        }

        composable(
            route = "details/plac/{detailId}",
            arguments = listOf(
                navArgument("detailId"){type = NavType.StringType}
            )
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("detailId")
            val place : LocalPlace = places.first { it.id == id }

            val map = HashMap<String, String>()

            Details(
                caracteristicas = map.toMap(),
                title = place.name,
                maxPg = null,
                pg = null,
                picture = place.picture,
                onNotes = { navController.navigate("notes/${place.id}/notes") },
                onBack = { navController.navigate("campaign/${place.campaign}/map") }
            )
        }

    }

}