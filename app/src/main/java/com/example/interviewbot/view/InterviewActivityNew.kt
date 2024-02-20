package com.example.interviewbot.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.interviewbot.utils.Question
import com.example.interviewbot.utils.Questions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.prefs.Preferences

class InterviewActivityNew : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomAppBarActivity()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomAppBarActivity() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar() {
                NavigationBarItem(
                    selected = navController.currentDestination?.route == "Home",
                    onClick = {
                        navController.navigate("Home") {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = navController.currentDestination?.route == "Quick",
                    onClick = {
                        navController.navigate("Quick") {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites") }
                )
                NavigationBarItem(
                    selected = navController.currentDestination?.route == "Settings",
                    onClick = {
                        navController.navigate("Settings") {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
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
        }
    }
}


@Composable
fun Home(navController: NavHostController) {
    Text("Home")
}

@Composable
fun Quick(navController: NavHostController) {
    StaggeredCategories(navController)
}

@Composable
fun Settings(navController: NavHostController) {
    Text("Settings")
}