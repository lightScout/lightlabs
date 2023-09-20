package com.lightscout.lightlabs.compose.animaiton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lightscout.lightlabs.R

@Preview
@Composable
fun ImageBorderAnimation() {

    val backGround = linearGradient(
        colors = listOf(
            Color(0xFF16415B),
            Color(0xFF19576A),
            Color(0xFF1C8A96),
            Color(0xFF178F9D),
            Color(0xFF19576A),
            Color(0xFF16415B)
        )
    )

    val brush = linearGradient(
        colors = listOf(
            Color(0xFF4DC9E6),
            Color(0xFF1DA5ED),
            Color(0xFF2897F0),
            Color(0xFF338AF2),
            Color(0xFF304BC1),
            Color(0xFF282CB7),
            Color(0xFF210CAE),
        )
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotationAnnotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            )
        ), label = ""
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backGround),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.cat1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(250.dp)
                .drawBehind {
                    rotate(rotationAnnotation.value) {
                        drawCircle(
                            brush = brush, style = Stroke(100f)
                        )
                    }

                }
                .clip(
                    CircleShape
                )
        )
    }
}