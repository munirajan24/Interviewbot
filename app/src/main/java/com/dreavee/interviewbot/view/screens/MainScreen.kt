package com.dreavee.interviewbot.view.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dreavee.interviewbot.view.Home
import com.dreavee.interviewbot.view.Quick
import com.dreavee.interviewbot.view.Settings
import com.dreavee.interviewbot.view.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
//            NavigationBar() {
//                NavigationBarItem(
//                    selected = navController.currentDestination?.route == "Home",
//                    onClick = {
//                        navController.navigate("Home") {
//                            launchSingleTop = true
//                            popUpTo(navController.graph.startDestinationId)
//                        }
//                    },
//                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
//                    label = { Text("Home") }
//                )
//                NavigationBarItem(
//                    selected = navController.currentDestination?.route == "Quick",
//                    onClick = {
//                        navController.navigate("Quick") {
//                            launchSingleTop = true
//                            popUpTo(navController.graph.startDestinationId)
//                        }
//                    },
//                    icon = { Icon(Icons.Filled.List, contentDescription = "Quick") },
//                    label = { Text("Quick") }
//                )
//                NavigationBarItem(
//                    selected = navController.currentDestination?.route == "Settings",
//                    onClick = {
//                        navController.navigate("Settings") {
//                            launchSingleTop = true
//                            popUpTo(navController.graph.startDestinationId)
//                        }
//                    },
//                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
//                    label = { Text("Settings") }
//                )
//            }
        }
    ) {it

        NavHost(navController, startDestination = "Quick") {
            composable("Home") { Home(navController) }
            composable("Quick") { Quick(navController) }
            composable("Settings") { Settings(navController) }
            composable(
                "next/{data}",
                arguments = listOf(navArgument("data") { type = NavType.StringType })
            ) { backStackEntry ->
                val dataJson = backStackEntry.arguments?.getString("data")
                val dataList = Gson().fromJson<List<Question>>(dataJson, object : TypeToken<List<Question>>() {}.type)
                NextComposable(selectedCategories = dataList)
            }
            composable(
                "InterviewSpeaker/{data}",
                arguments = listOf(navArgument("data") { type = NavType.StringType })
            ) { backStackEntry ->
                val dataJson = backStackEntry.arguments?.getString("data")
                val dataList = Gson().fromJson<List<String>>(dataJson, object : TypeToken<List<String>>() {}.type)
                InterviewSpeaker(selectedCategories = dataList)
            }
        }
    }
}
