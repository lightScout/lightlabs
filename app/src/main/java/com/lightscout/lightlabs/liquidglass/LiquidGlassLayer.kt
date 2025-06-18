package com.lightscout.lightlabs.liquidglass

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Represents different rendering layers for liquid glass effects. Inspired by Flutter's
 * liquid_glass_layer architecture.
 */
sealed class LiquidGlassLayer {
    abstract fun render(
            drawScope: DrawScope,
            settings: LiquidGlassSettings,
            rect: androidx.compose.ui.geometry.Rect
    )
}

/** Background blur and frost layer */
class BackdropLayer : LiquidGlassLayer() {
    override fun render(
            drawScope: DrawScope,
            settings: LiquidGlassSettings,
            rect: androidx.compose.ui.geometry.Rect
    ) {
        with(drawScope) {
            // Ultra-subtle backdrop effect
            drawRoundRect(
                    color = Color.White.copy(alpha = settings.frostStrength * 0.15f),
                    cornerRadius =
                            androidx.compose.ui.geometry.CornerRadius(settings.borderRadius.toPx()),
                    blendMode = BlendMode.Overlay
            )
        }
    }
}

/** Main glass material layer with realistic optical properties */
class GlassMaterialLayer : LiquidGlassLayer() {
    override fun render(
            drawScope: DrawScope,
            settings: LiquidGlassSettings,
            rect: androidx.compose.ui.geometry.Rect
    ) {
        with(drawScope) {
            // Primary glass material
            drawRoundRect(
                    color = settings.baseColor,
                    cornerRadius =
                            androidx.compose.ui.geometry.CornerRadius(settings.borderRadius.toPx()),
                    blendMode = BlendMode.Multiply
            )

            // Subtle material depth
            val depthGradient =
                    Brush.radialGradient(
                            colors =
                                    listOf(
                                            Color.White.copy(
                                                    alpha = settings.baseColor.alpha * 0.3f
                                            ),
                                            Color.Transparent
                                    ),
                            center =
                                    androidx.compose.ui.geometry.Offset(
                                            rect.width * 0.3f,
                                            rect.height * 0.2f
                                    ),
                            radius = rect.width * 0.6f
                    )

            drawRoundRect(
                    brush = depthGradient,
                    cornerRadius =
                            androidx.compose.ui.geometry.CornerRadius(settings.borderRadius.toPx()),
                    blendMode = BlendMode.Plus
            )
        }
    }
}

/** Specular highlight layer for light reflection */
class SpecularLayer : LiquidGlassLayer() {
    override fun render(
            drawScope: DrawScope,
            settings: LiquidGlassSettings,
            rect: androidx.compose.ui.geometry.Rect
    ) {
        if (settings.specularIntensity <= 0f) return

        with(drawScope) {
            // Single, realistic highlight
            val highlight =
                    Brush.radialGradient(
                            colors =
                                    listOf(
                                            Color.White.copy(
                                                    alpha = settings.specularIntensity * 0.6f
                                            ),
                                            Color.White.copy(
                                                    alpha = settings.specularIntensity * 0.2f
                                            ),
                                            Color.Transparent
                                    ),
                            center =
                                    androidx.compose.ui.geometry.Offset(
                                            rect.width * 0.25f,
                                            rect.height * 0.15f
                                    ),
                            radius = rect.width * 0.3f
                    )

            drawCircle(
                    brush = highlight,
                    radius = rect.width * 0.15f,
                    center =
                            androidx.compose.ui.geometry.Offset(
                                    rect.width * 0.25f,
                                    rect.height * 0.15f
                            ),
                    blendMode = BlendMode.Plus
            )
        }
    }
}

/** Border and edge definition layer */
class BorderLayer : LiquidGlassLayer() {
    override fun render(
            drawScope: DrawScope,
            settings: LiquidGlassSettings,
            rect: androidx.compose.ui.geometry.Rect
    ) {
        if (settings.bezelWidth <= 0.dp) return

        with(drawScope) {
            val strokeWidth = settings.bezelWidth.toPx()
            val cornerRadius = settings.borderRadius.toPx()

            // Clean, minimal border
            drawRoundRect(
                    color = settings.bezelColor,
                    topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2, strokeWidth / 2),
                    size =
                            androidx.compose.ui.geometry.Size(
                                    rect.width - strokeWidth,
                                    rect.height - strokeWidth
                            ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
                    style = Stroke(width = strokeWidth),
                    blendMode = BlendMode.Plus
            )
        }
    }
}

/** Manages and renders all liquid glass layers in the correct order */
class LiquidGlassLayerRenderer {
    private val layers =
            listOf(BackdropLayer(), GlassMaterialLayer(), SpecularLayer(), BorderLayer())

    fun renderLayers(
            drawScope: DrawScope,
            settings: LiquidGlassSettings,
            rect: androidx.compose.ui.geometry.Rect
    ) {
        layers.forEach { layer -> layer.render(drawScope, settings, rect) }
    }
}
