package com.example.notasmazmorras.ui.views

import androidx.compose.runtime.Composable
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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){

        composable(route = "login"){
            Login(navController)
        }

        composable(route = "createAccount"){
            CreateAccount(navController)
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
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            Account(navController)
        }

        composable(route = "createCampaing"){
            CreateCampaign(navController)
        }

        composable(
            route = "campaign/{id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            Campaign(navController)
        }

        composable(
            route = "campaign/{id}/calendar",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            Calendar(navController)
        }

        composable(
            route = "campaign/{id}/players",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
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
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            Note(navController)
        }

        composable(
            route = "campaign/{id}/characters",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            Characters(navController)
        }

        composable(
            route = "campaign/{id}/objects",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            Objects(navController)
        }

        composable(
            route = "campaign/{id}/creatures",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            Creatures(navController)
        }

        composable(
            route = "campaign/{id}/map",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            Map(navController)
        }

        composable(
            route = "editCharacter/{id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            EditCharacter(navController)
        }

        composable(
            route = "editObject/{id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            EditObject(navController)
        }

        composable(
            route = "editCreature/{id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            EditCreature(navController)
        }

        composable(
            route = "editMap/{id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            EditMap(navController)
        }

        composable(
            route = "details/{detailType}/{detailId}",
            arguments = listOf(
                navArgument("detailType"){type = NavType.StringType},
                navArgument("detailId"){type = NavType.IntType}
            )
        ){
            Details(navController)
        }

    }

}