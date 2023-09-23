package com.lightscout.lightlabs.compose.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Lines() {

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black), onDraw = {
        drawLine(
            start = Offset(100f, 620f),
            end = Offset(100f, 1790f),
            color = Color(0xFFFFFFFF),
            strokeWidth = 10f
        )

        drawLine(
            start = Offset(675f, 1200f),
            end = Offset(675f, 1790f),
            color = Color(0xFFFFFFFF),
            strokeWidth = 10f
        )
        drawArc(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF),
                )
            ),
            topLeft = Offset(100f, 1500f),
            size = Size(575f, 575f),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(
                width = 10f
            ),
        )


    })

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.offset(x = 30.dp, y = 16.dp),
            text = "flow.",
            color = Color(0xFFFFFFFF),
            style = MaterialTheme.typography.displaySmall,
            fontFamily = FontFamily.SansSerif
        )

        Text(
            modifier = Modifier.offset(x = 30.dp, y = 130.dp),
            text = "welcome to\ndesign (studio*)",
            color = Color(0xFFFFFFFF),
            style = MaterialTheme.typography.displaySmall,
            fontFamily = FontFamily.SansSerif
        )

    }

}