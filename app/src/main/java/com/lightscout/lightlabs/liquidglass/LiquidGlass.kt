package com.lightscout.lightlabs.liquidglass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * A Liquid Glass container that wraps content with Apple's Liquid Glass effect. Features
 * translucent glass material with blur, refraction, and dynamic lighting. Now with optional
 * RenderScript GPU acceleration for real backdrop blur.
 *
 * @param modifier Modifier to be applied to the container
 * @param settings Configuration for the glass effect
 * @param onClick Optional click handler
 * @param enabled Whether the glass is interactive
 * @param useRenderScript Enable GPU-accelerated blur via RenderScript
 * @param content The content to be displayed inside the glass container
 */
@Composable
fun LiquidGlass(
        modifier: Modifier = Modifier,
        settings: LiquidGlassSettings = LiquidGlassSettings(),
        onClick: (() -> Unit)? = null,
        enabled: Boolean = true,
        useRenderScript: Boolean = true,
        content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by
            animateFloatAsState(
                    targetValue = if (isPressed) settings.touchScale else 1f,
                    animationSpec = tween(durationMillis = settings.animationDuration),
                    label = "liquid_glass_scale"
            )

    val interactionSource = remember { MutableInteractionSource() }
    val density = LocalDensity.current

    val shape = RoundedCornerShape(settings.borderRadius)

    Box(
            modifier =
                    modifier.scale(scale)
                            .clip(shape)
                            .then(
                                    if (onClick != null) {
                                        Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                enabled = enabled,
                                                onClick = onClick
                                        )
                                    } else Modifier
                            )
                            .then(
                                    if (useRenderScript) {
                                        Modifier.liquidGlassBackdrop(
                                                blurRadius = settings.blurRadius,
                                                glassColor = settings.baseColor,
                                                quality =
                                                        when (settings.quality) {
                                                            LiquidGlassQuality.PERFORMANCE ->
                                                                    RenderScriptBlur.BlurQuality
                                                                            .FAST
                                                            LiquidGlassQuality.BALANCED ->
                                                                    RenderScriptBlur.BlurQuality
                                                                            .STANDARD
                                                            LiquidGlassQuality.PREMIUM ->
                                                                    RenderScriptBlur.BlurQuality
                                                                            .LIQUID
                                                        }
                                        )
                                    } else {
                                        Modifier.liquidGlassEffect(settings)
                                    }
                            )
    ) { content() }
}

/** Custom modifier that applies the liquid glass visual effect */
private fun Modifier.liquidGlassEffect(settings: LiquidGlassSettings): Modifier {
    return this.drawWithContent {
        // Draw the content first
        drawContent()

        // Apply advanced liquid glass overlay with realistic optical effects
        drawAdvancedLiquidGlass(settings)
    }
}

/** Draws advanced liquid glass effect with realistic optical properties */
private fun DrawScope.drawAdvancedLiquidGlass(settings: LiquidGlassSettings) {
    val rect = androidx.compose.ui.geometry.Rect(Offset.Zero, size)
    val layerRenderer = LiquidGlassLayerRenderer()

    // Use layered architecture inspired by Flutter implementation
    drawIntoCanvas { canvas ->
        layerRenderer.renderLayers(this@drawAdvancedLiquidGlass, settings, rect)
    }
}

/** Draws simple glass base material */
private fun DrawScope.drawSimpleGlassBase(
        settings: LiquidGlassSettings,
        rect: androidx.compose.ui.geometry.Rect
) {
    // Very subtle glass material
    drawRoundRect(
            color = settings.baseColor,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(settings.borderRadius.toPx()),
            blendMode = BlendMode.Overlay
    )

    // Add just a hint of frost
    if (settings.frostStrength > 0f) {
        drawRoundRect(
                color = Color.White.copy(alpha = settings.frostStrength * 0.3f),
                cornerRadius =
                        androidx.compose.ui.geometry.CornerRadius(settings.borderRadius.toPx()),
                blendMode = BlendMode.Overlay
        )
    }
}

/** Draws a single, subtle highlight */
private fun DrawScope.drawSubtleHighlight(
        settings: LiquidGlassSettings,
        rect: androidx.compose.ui.geometry.Rect
) {
    if (settings.specularIntensity > 0f) {
        val highlight =
                Brush.radialGradient(
                        colors =
                                listOf(
                                        Color.White.copy(alpha = settings.specularIntensity * 0.4f),
                                        Color.White.copy(alpha = settings.specularIntensity * 0.1f),
                                        Color.Transparent
                                ),
                        center = Offset(rect.width * 0.3f, rect.height * 0.2f),
                        radius = rect.width * 0.4f
                )

        drawCircle(
                brush = highlight,
                radius = rect.width * 0.2f,
                center = Offset(rect.width * 0.3f, rect.height * 0.2f),
                blendMode = BlendMode.Plus
        )
    }
}

/** Draws the luminous border around the glass container */
private fun DrawScope.drawLiquidGlassBorder(settings: LiquidGlassSettings) {
    if (settings.bezelWidth > 0.dp) {
        val strokeWidth = settings.bezelWidth.toPx()
        val cornerRadius = settings.borderRadius.toPx()

        // Simple border with subtle gradient
        val borderBrush =
                Brush.linearGradient(
                        colors =
                                listOf(
                                        settings.bezelColor.copy(alpha = 0.6f),
                                        settings.bezelColor.copy(alpha = 0.2f),
                                        settings.bezelColor.copy(alpha = 0.6f)
                                ),
                        start = Offset.Zero,
                        end = Offset(size.width, size.height)
                )

        // Single clean border
        drawRoundRect(
                brush = borderBrush,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size =
                        androidx.compose.ui.geometry.Size(
                                size.width - strokeWidth,
                                size.height - strokeWidth
                        ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
                style = Stroke(width = strokeWidth),
                blendMode = BlendMode.Overlay
        )
    }
}
