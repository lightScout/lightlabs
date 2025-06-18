package com.lightscout.lightlabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lightscout.lightlabs.liquidglass.LiquidGlassDemo
import com.lightscout.lightlabs.ui.theme.LightLabsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LightLabsTheme { LiquidGlassDemo() } }
    }
}

@Preview
@Composable
fun LabPreview() {
    LightLabsTheme { LiquidGlassDemo() }
}
