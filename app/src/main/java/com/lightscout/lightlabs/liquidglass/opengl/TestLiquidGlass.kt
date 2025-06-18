package com.lightscout.lightlabs.liquidglass.opengl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.lightscout.lightlabs.liquidglass.LiquidGlassQuality

/**
 * Test implementation of liquid glass effects for development. This provides a visual preview
 * without requiring OpenGL.
 */
@Composable
fun TestRealTimeLiquidGlass(
        quality: LiquidGlassQuality = LiquidGlassQuality.BALANCED,
        blurRadius: Float = 10f,
        refractionStrength: Float = 0.5f,
        chromaticAberration: Float = 1.0f,
        glassThickness: Float = 0.2f,
        glassColor: Color = Color.White,
        modifier: Modifier = Modifier,
        backgroundContent: @Composable () -> Unit,
        glassContent: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        // Background layer
        backgroundContent()

        // Glass effect simulation layer
        Box(
                modifier =
                        Modifier.fillMaxSize()
                                .background(
                                        brush =
                                                Brush.radialGradient(
                                                        colors =
                                                                listOf(
                                                                        glassColor.copy(
                                                                                alpha = 0.1f
                                                                        ),
                                                                        glassColor.copy(
                                                                                alpha = 0.05f
                                                                        ),
                                                                        Color.Transparent
                                                                )
                                                )
                                )
        )

        // Content layer
        glassContent()
    }
}

/** Test version of the shader view that falls back to software blur */
@Composable
fun TestLiquidGlassShaderView(
        backgroundBitmap: android.graphics.Bitmap?,
        quality: LiquidGlassQuality = LiquidGlassQuality.BALANCED,
        blurRadius: Float = 10f,
        refractionStrength: Float = 0.5f,
        chromaticAberration: Float = 1.0f,
        glassThickness: Float = 0.2f,
        glassColor: Color = Color.White,
        modifier: Modifier = Modifier,
        onViewCreated: ((Any) -> Unit)? = null
) {
    // Simple glass effect placeholder
    Box(
            modifier =
                    modifier.background(
                            brush =
                                    Brush.linearGradient(
                                            colors =
                                                    listOf(
                                                            glassColor.copy(alpha = 0.15f),
                                                            glassColor.copy(alpha = 0.08f),
                                                            glassColor.copy(alpha = 0.15f)
                                                    )
                                    )
                    )
    )
}
