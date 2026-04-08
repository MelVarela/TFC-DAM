package com.example.notasmazmorras.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.example.notasmazmorras.ui.views.campaign.details.Map
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
            Login(navController)
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
            Home(navController)
        }

        composable(route = "invitations"){
            Invitations(navController)
        }

        composable(
            route = "reportingSugestions/{action}",
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

        composable(route = "createCampaing"){
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
            Campaign(navController)
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
            route = "notes/{type}/{typeId}/notes",
            arguments = listOf(
                navArgument("typeId"){type = NavType.IntType},
                navArgument("type"){type = NavType.StringType}
            )
        ){
            Notes(navController)
        }

        composable(
            route = "note/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Note(navController)
        }

        composable(
            route = "campaign/{id}/characters",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Characters(navController)
        }

        composable(
            route = "campaign/{id}/objects",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Objects(navController)
        }

        composable(
            route = "campaign/{id}/creatures",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Creatures(navController)
        }

        composable(
            route = "campaign/{id}/map",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){
            Map(navController)
        }

        composable(
            route = "editCharacter/{id}",
            arguments = listOf(navArgument("id"){type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            EditCharacter(
                onDone = {
                    character -> characterViewmodel.updateCharacter(character)
                    navController.navigate("campaign/" + campaignViewmodel.currentCampaign + "/characters")
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
                    navController.navigate("campaign/" + campaignViewmodel.currentCampaign + "/characters")
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
                    obxecto -> objectViewmodel.insertObject(obxecto)
                    navController.navigate("campaign/" + campaignViewmodel.currentCampaign + "/objects")
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
                    navController.navigate("campaign/" + campaignViewmodel.currentCampaign + "/objects")
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
                    creature -> creatureViewmodel.insertCreature(creature)
                    navController.navigate("campaign/" + campaignViewmodel.currentCampaign + "/creatures")
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
                    navController.navigate("campaign/" + campaignViewmodel.currentCampaign + "/creatures")
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
                    place -> placeViewmodel.insertPlace(place)
                    navController.navigate("campaign/" + campaignViewmodel.currentCampaign + "/map")
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
                    navController.navigate("campaign/" + campaignViewmodel.currentCampaign + "/map")
                },
                places = places,
                placeId = null,
                campaign = campaignViewmodel.currentCampaign,
                navController
            )
        }

        composable(
            route = "details/{detailType}/{detailId}",
            arguments = listOf(
                navArgument("detailType"){type = NavType.StringType},
                navArgument("detailId"){type = NavType.StringType}
            )
        ){
            Details(navController)
        }

    }

}