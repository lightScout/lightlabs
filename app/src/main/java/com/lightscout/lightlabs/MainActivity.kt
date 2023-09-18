package com.lightscout.lightlabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lightscout.lightlabs.compose.animaiton.AnimateFloatAsState
import com.lightscout.lightlabs.compose.animaiton.AnimatedContentSize
import com.lightscout.lightlabs.compose.animaiton.AnimatedVisibility
import com.lightscout.lightlabs.ui.theme.LightLabsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LightLabsTheme {
                // A surface container using the 'background' color from the theme
                AnimateFloatAsState()
            }
        }
    }
}
