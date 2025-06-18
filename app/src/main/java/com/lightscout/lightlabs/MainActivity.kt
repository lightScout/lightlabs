package com.lightscout.lightlabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lightscout.lightlabs.ui.screens.EventListScreen
import com.lightscout.lightlabs.ui.screens.EventMapScreen
import com.lightscout.lightlabs.ui.theme.LightLabsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LightLabsTheme { EventPlatformApp() } }
    }
}

@Composable
fun EventPlatformApp() {
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "event_list") {
            composable("event_list") {
                EventListScreen(onNavigateToMap = { navController.navigate("event_map") })
            }

            composable("event_map") { EventMapScreen() }
        }
    }
}
