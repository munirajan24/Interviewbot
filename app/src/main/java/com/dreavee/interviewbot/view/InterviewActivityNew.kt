package com.dreavee.interviewbot.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dreavee.interviewbot.view.model.Question
import com.dreavee.interviewbot.view.screens.InterviewSpeaker
import com.dreavee.interviewbot.view.screens.NextComposable
import com.dreavee.interviewbot.view.screens.Categories
import com.dreavee.interviewbot.view.screens.MainActivityScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class InterviewActivityNew : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityScreen()
        }
    }
}



@Composable
fun Home(navController: NavHostController) {
    Text("Home")
}

@Composable
fun Quick(navController: NavHostController) {
    Categories(navController)
}

@Composable
fun Settings(navController: NavHostController) {
    Text("Settings")
}