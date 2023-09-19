package com.lightscout.lightlabs.compose.animaiton

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.lightscout.lightlabs.util.Util


@Preview
@Composable
fun Ripple() {
    // Remember the infinite transition for animations.
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Configure the infinite ripple radius animation.
    val animatedRippleRadius by infiniteTransition.animateFloat(
        initialValue = 2f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 3400, easing = EaseOut),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    // Configure the infinite ripple alpha animation.
    val animatedRippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1700, easing = EaseOut),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Convert the hex color list to a list of Color objects.
    val colorList = Util().convertHexListToColorList(
        listOf(
            "03045e", "023e8a", "0077b6", "0096c7", "00b4d8", "48cae4", "90e0ef", "ade8f4", "03045e",
            "03045e", "023e8a", "0077b6",
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Initialize alpha factor to modulate color transparency.
        var alphaFactor = 1f
        // Define decrement factor to determine how much to decrease alpha with each iteration.
        val decrementFactor = .05f / colorList.size

        // Loop through each color and draw a circle with varying alpha and radius.
        for ((index, color) in colorList.withIndex()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = color.copy(alpha = animatedRippleAlpha * alphaFactor),
                    radius = animatedRippleRadius * ((index * 1.5) + 1).toFloat(),
                    center = center,
                    style = Stroke(width = 12f, cap = StrokeCap.Round),
                )
            }
            alphaFactor -= decrementFactor // Decrease the alpha factor for the next circle.
        }
    }
}
