package com.lightscout.lightlabs.compose.animaiton

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnimateFloatAsState() {

    val brush = linearGradient(
        colors = listOf(
            Color(0xFF12B3EB),
            Color(0xFF1DA5ED),
            Color(0xFF2897F0),
            Color(0xFF338AF2),
            Color(0xFF3E7CF4),
            Color(0xFF496EF7),
            Color(0xFF5460F9),
        )
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        var index by remember { mutableStateOf(32) }
        val size by animateIntAsState(targetValue = index, label = "")

        LargeFloatingActionButton(
            onClick = { index += 150 },
            shape = CircleShape,
            containerColor = Color(0xFFC6F8FF),
            modifier = Modifier.size(size.dp).indication(
                indication = null,
                interactionSource = MutableInteractionSource()
            )
        ) {

        }
    }


}