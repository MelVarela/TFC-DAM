package com.example.notasmazmorras.ui.views

import android.util.Log
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
import com.example.notasmazmorras.ui.views.campaign.CreateCampaign
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
import com.example.notasmazmorras.viewmodels.UserViewmodel
import kotlinx.coroutines.awaitAll

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val userViewmodel : UserViewmodel = viewModel(factory = UserViewmodel.Factory)
    val campaignViewmodel : CampaignViewmodel = viewModel(factory = CampaignViewmodel.Factory)
    val characterViewmodel : CharacterViewmodel = viewModel(factory = CharacterViewmodel.Factory)
    val creatureViewmodel : CreatureViewmodel = viewModel(factory = CreatureViewmodel.Factory)
    val noteViewmodel : NoteViewmodel = viewModel(factory = NoteViewmodel.Factory)
    val objectViewmodel : ObjectViewmodel = viewModel(factory = ObjectViewmodel.Factory)
    val placeViewmodel : PlaceViewmodel = viewModel(factory = PlaceViewmodel.Factory)

    val users by userViewmodel.users.collectAsState()
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

        composable(route = "login"){
            Login(
                authenticated = userViewmodel.authenticated.collectAsState().value,
                onSuccess = {
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
                    user -> userViewmodel.insertUser(user)
                    navController.navigate("home")
                         },
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
                    id -> navController.navigate("campaign/${id}")
                },
                onSync = {
                    campaignViewmodel.sync(userViewmodel.currentUser)
                    for (campaign in campaigns) {
                        if(campaign.id.substring(0, 1) != "l"){
                            characterViewmodel.sync(campaign.id)
                            creatureViewmodel.sync(campaign.id)
                            noteViewmodel.sync(campaign.id)
                            objectViewmodel.sync(campaign.id)
                            placeViewmodel.sync(campaign.id)
                        }
                    }
                },
                navController
            )
        }

        composable(route = "invitations"){
            Invitations(navController)
        }

        composable(
            route = "reportingSuggestions/{action}",
            arguments = listOf(navArgument("action"){type = NavType.StringType})
        ){
            ReportSuggest(navController)
        }

        composable(route = "options"){
            Options(navController)
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
                    campaign -> campaignViewmodel.insertCampaign(campaign)
                    campaignViewmodel.currentCampaign = campaign.id
                    navController.navigate("campaign/" + campaign.id)
                         },
                navController
            )
        }

        composable(
            route = "campaign/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            campaignViewmodel.currentCampaign = id!!

            Log.d("CA", id)
            Log.d("CAS", campaigns.toString())

            var campaign : LocalCampaign

            try{
                campaign = campaigns.first{ it.id == id }
            }catch (e: Throwable){
                navController.navigate("home")
            }

            Campaign(
                campaign = campaigns.first{ it.id == id },
                onBack = {navController.navigate("home")},
                onSync = {
                    campaignViewmodel.sync(id)
                    characterViewmodel.sync(id)
                    creatureViewmodel.sync(id)
                    noteViewmodel.sync(id)
                    objectViewmodel.sync(id)
                    placeViewmodel.sync(id)
                },
                navController
            )
        }

        composable(
            route = "campaign/{id}/calendar",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Calendar(navController)
        }

        composable(
            route = "campaign/{id}/players",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Players(navController)
        }

        composable(
            route = "notes/{owner}/notes",
            arguments = listOf(
                navArgument("owner"){type = NavType.StringType}
            )
        ){ backStackEntry ->
            val ownerId = backStackEntry.arguments?.getString("owner")!!

            Notes(
                notes = notes.filter { it.owner == ownerId },
                onPress = { id -> navController.navigate("note/${id}") },
                onDelete = { note -> noteViewmodel.deleteNote(note) },
                onNew = { navController.navigate("newNote/${ownerId}") },
                onSync = {
                    noteViewmodel.sync(ownerId)
                },
                onBack = {
                    Log.d("Nav", "Owner ID: $ownerId")
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
                }
            )
        }

        composable(
            route = "note/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")

            Note(
                note = notes.first{ it.id == id },
                onBack = {
                    note -> noteViewmodel.updateNote(note)
                    navController.navigate("notes/${note.owner}/notes")
                },
                ""
            )
        }

        composable(
            route = "newNote/{ownerId}",
            arguments = listOf(navArgument("ownerId"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("ownerId")

            Note(
                note = null,
                onBack = {
                    note -> noteViewmodel.insertNote(note)
                    navController.navigate("notes/${note.owner}/notes")
                },
                owner = id!!
            )
        }

        composable(
            route = "campaign/{id}/characters",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val campId = backStackEntry.arguments?.getString("id")

            Characters(
                characters = characters,
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
                    characterViewmodel.sync(campaignViewmodel.currentCampaign)
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
                obxectos = objects,
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
                    objectViewmodel.sync(campaignViewmodel.currentCampaign)
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
                creatures = creatures,
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
                    creatureViewmodel.sync(campaignViewmodel.currentCampaign)
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
                places = places,
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
                    placeViewmodel.sync(campaignViewmodel.currentCampaign)
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
                    navController.navigate("campaign/${character.campaign}/characters")
                },
                characters = characters,
                characterId = id,
                campaign = campaignViewmodel.currentCampaign,
                navController
            )
        }

        composable(
            route = "editCharacter"
        ){
            EditCharacter(
                onDone = {
                    character -> characterViewmodel.insertCharacter(character)
                    navController.navigate("campaign/${character.campaign}/characters")
                },
                characters = characters,
                characterId = null,
                campaign = campaignViewmodel.currentCampaign,
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
                    navController.navigate("campaign/${obxecto.campaign}/objects")
                },
                objects = objects,
                objectId = id,
                campaign = campaignViewmodel.currentCampaign,
                navController
            )
        }

        composable(
            route = "editObject"
        ){
            EditObject(
                onDone = {
                    obxecto -> objectViewmodel.insertObject(obxecto)
                    navController.navigate("campaign/${obxecto.campaign}/objects")
                },
                objects = objects,
                objectId = null,
                campaign = campaignViewmodel.currentCampaign,
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
                    navController.navigate("campaign/${creature.campaign}/creatures")
                },
                creatures = creatures,
                creatureId = id,
                campaign = campaignViewmodel.currentCampaign,
                navController
            )
        }

        composable(
            route = "editCreature"
        ){
            EditCreature(
                onDone = {
                    creature -> creatureViewmodel.insertCreature(creature)
                    navController.navigate("campaign/${creature.campaign}/creatures")
                },
                creatures = creatures,
                creatureId = null,
                campaign = campaignViewmodel.currentCampaign,
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
                    navController.navigate("campaign/${place.campaign}/map")
                },
                places = places,
                placeId = id,
                campaign = campaignViewmodel.currentCampaign,
                navController
            )
        }

        composable(
            route = "editMap"
        ){
            EditMap(
                onDone = {
                    place -> placeViewmodel.insertPlace(place)
                    navController.navigate("campaign/${place.campaign}/map")
                },
                places = places,
                placeId = null,
                campaign = campaignViewmodel.currentCampaign,
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