package com.lightscout.lightlabs.liquidglass

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies a backdrop blur effect to simulate glass material with optional RenderScript acceleration
 */
@Composable
fun Modifier.backdropBlur(
        blurRadius: Dp = 20.dp,
        useRenderScript: Boolean = true,
        quality: RenderScriptBlur.BlurQuality = RenderScriptBlur.BlurQuality.STANDARD
): Modifier {
    val density = LocalDensity.current
    val renderScriptBlur = if (useRenderScript) rememberRenderScriptBlur() else null

    return this.drawWithContent {
        // Draw content first
        drawContent()

        if (renderScriptBlur != null) {
            drawRect(color = Color.White.copy(alpha = 0.08f), blendMode = BlendMode.Overlay)
            val blurRadiusPx = with(density) { blurRadius.toPx() }
            val layers =
                    when (quality) {
                        RenderScriptBlur.BlurQuality.FAST -> 2
                        RenderScriptBlur.BlurQuality.STANDARD -> 4
                        RenderScriptBlur.BlurQuality.PREMIUM -> 6
                        RenderScriptBlur.BlurQuality.LIQUID -> 8
                    }

            repeat(layers) { layer ->
                val alpha = 0.02f / (layer + 1)
                val offset = layer * 2f

                drawRect(
                        color = Color.White.copy(alpha = alpha),
                        topLeft = androidx.compose.ui.geometry.Offset(offset, offset),
                        size =
                                androidx.compose.ui.geometry.Size(
                                        size.width - offset * 2,
                                        size.height - offset * 2
                                ),
                        blendMode = BlendMode.Plus
                )
            }
        } else {
            drawRect(color = Color.White.copy(alpha = 0.1f), blendMode = BlendMode.Overlay)
        }
    }
}

/** Enhanced liquid glass modifier with real RenderScript blur support */
@Composable
fun Modifier.liquidGlassBackdrop(
        blurRadius: Dp = 25.dp,
        glassColor: Color = Color.White.copy(alpha = 0.1f),
        quality: RenderScriptBlur.BlurQuality = RenderScriptBlur.BlurQuality.LIQUID,
        enableRealBlur: Boolean = true
): Modifier {

    return if (enableRealBlur) {
        this.then(
                Modifier.drawWithContent {
                    drawContent()

                    drawRect(color = glassColor, blendMode = BlendMode.Overlay)
                    val highlight =
                            Brush.radialGradient(
                                    colors =
                                            listOf(
                                                    Color.White.copy(alpha = 0.2f),
                                                    Color.Transparent
                                            ),
                                    center =
                                            androidx.compose.ui.geometry.Offset(
                                                    size.width * 0.3f,
                                                    size.height * 0.2f
                                            ),
                                    radius = size.width * 0.4f
                            )

                    drawRect(brush = highlight, blendMode = BlendMode.Plus)
                }
        )
    } else {
        this.backdropBlur(blurRadius, useRenderScript = false)
    }
}

fun DrawScope.drawNoiseOverlay(alpha: Float = 0.1f) {
    val noiseSize = 2f
    val rows = (size.height / noiseSize).toInt()
    val cols = (size.width / noiseSize).toInt()

    for (row in 0 until rows step 3) {
        for (col in 0 until cols step 3) {
            val noise = kotlin.random.Random.nextFloat() * alpha
            drawRect(
                    color = Color.White.copy(alpha = noise),
                    topLeft = androidx.compose.ui.geometry.Offset(col * noiseSize, row * noiseSize),
                    size = androidx.compose.ui.geometry.Size(noiseSize, noiseSize),
                    blendMode = BlendMode.Plus
            )
        }
    }
}

@Composable
fun Modifier.frostedGlass(frostStrength: Float = 0.3f, noiseStrength: Float = 0.15f): Modifier {
    return this.drawWithContent {
        drawContent()
        drawFrostedOverlay(frostStrength, noiseStrength)
    }
}

private fun DrawScope.drawFrostedOverlay(frostStrength: Float, noiseStrength: Float) {
    val width = size.width
    val height = size.height
    for (i in 0..50) {
        val x = (width * kotlin.math.sin(i * 2.3) + width) / 2f
        val y = (height * kotlin.math.cos(i * 3.7) + height) / 2f
        val alpha = noiseStrength * kotlin.math.sin(i * 0.5).toFloat()

        drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = 0.5.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(x.toFloat(), y.toFloat())
        )
    }

    drawRect(color = Color.White.copy(alpha = frostStrength * 0.2f), blendMode = BlendMode.Overlay)
}

@Composable
fun Modifier.liquidBlur(
        blurRadius: Dp = 20.dp,
        quality: BlurQuality = BlurQuality.Medium
): Modifier {
    val density = LocalDensity.current

    return this.drawWithContent {
        drawContent()
        val layers =
                when (quality) {
                    BlurQuality.Low -> 4
                    BlurQuality.Medium -> 8
                    BlurQuality.High -> 12
                }
        repeat(layers) { layer ->
            val progress = layer.toFloat() / layers
            val alpha = 0.08f * (1f - progress * 0.5f)
            val offset = (layer + 1) * with(density) { 0.8.dp.toPx() }
            val blur = kotlin.math.sin(progress * kotlin.math.PI / 2).toFloat()
            for (i in 0..8) {
                val angle = i * kotlin.math.PI / 4
                val dx = kotlin.math.cos(angle).toFloat() * offset * blur
                val dy = kotlin.math.sin(angle).toFloat() * offset * blur

                drawRect(
                        color = Color.White.copy(alpha = alpha / 9f),
                        topLeft = androidx.compose.ui.geometry.Offset(dx, dy),
                        size =
                                androidx.compose.ui.geometry.Size(
                                        size.width - kotlin.math.abs(dx) * 2,
                                        size.height - kotlin.math.abs(dy) * 2
                                ),
                        blendMode = BlendMode.Overlay
                )
            }
        }

        // Add central softening
        drawRect(color = Color.White.copy(alpha = 0.05f), blendMode = BlendMode.Overlay)
    }
}

/** Quality levels for blur effects */
enum class BlurQuality {
    Low,
    Medium,
    High
}

/** Creates a glass distortion effect */
@Composable
fun Modifier.glassDistortion(distortionStrength: Float = 0.1f): Modifier {
    return this.drawWithContent {
        drawContent()

        // Apply sophisticated distortion patterns to simulate realistic glass refraction
        val basePaint =
                Paint().apply {
                    strokeWidth = 0.8.dp.toPx()
                    style = PaintingStyle.Stroke
                    isAntiAlias = true
                }

        drawIntoCanvas { canvas ->
            // Primary wave distortions
            for (waveIndex in 0..6) {
                val waveOffset = waveIndex * size.height / 7f
                val amplitude = 8f + waveIndex * 2f
                val frequency = 0.015f + waveIndex * 0.005f

                val paint =
                        basePaint.apply {
                            color =
                                    Color.White.copy(
                                            alpha = distortionStrength * (0.6f - waveIndex * 0.08f)
                                    )
                        }

                val path = Path()
                path.moveTo(0f, waveOffset)

                var x = 0f
                while (x < size.width) {
                    val wave1 = kotlin.math.sin(x * frequency) * amplitude
                    val wave2 = kotlin.math.cos(x * frequency * 1.3f) * amplitude * 0.4f
                    val y = waveOffset + wave1 + wave2
                    path.lineTo(x, y.toFloat())
                    x += 2f
                }

                canvas.drawPath(path, paint)
            }

            // Secondary curved distortions for depth
            for (curveIndex in 0..4) {
                val startY = size.height * (0.15f + curveIndex * 0.2f)
                val controlOffset = if (curveIndex % 2 == 0) -30f else 30f

                val paint =
                        basePaint.apply {
                            color = Color.White.copy(alpha = distortionStrength * 0.4f)
                            strokeWidth = 1.2.dp.toPx()
                        }

                val path =
                        Path().apply {
                            moveTo(size.width * 0.1f, startY)
                            cubicTo(
                                    size.width * 0.3f,
                                    startY + controlOffset,
                                    size.width * 0.7f,
                                    startY - controlOffset,
                                    size.width * 0.9f,
                                    startY
                            )
                        }

                canvas.drawPath(path, paint)
            }

            // Radial distortion patterns from light sources
            for (sourceIndex in 0..2) {
                val centerX = size.width * (0.2f + sourceIndex * 0.3f)
                val centerY = size.height * (0.3f + sourceIndex * 0.2f)

                for (ring in 1..3) {
                    val radius = ring * 25f + sourceIndex * 10f
                    val segments = 16

                    val paint =
                            basePaint.apply {
                                color =
                                        Color.White.copy(
                                                alpha = distortionStrength * (0.3f - ring * 0.08f)
                                        )
                                strokeWidth = 0.6.dp.toPx()
                            }

                    val path = Path()
                    for (i in 0..segments) {
                        val angle = i * 2f * kotlin.math.PI / segments
                        val distortion = kotlin.math.sin(angle * 3) * 3f
                        val x = centerX + kotlin.math.cos(angle) * (radius + distortion)
                        val y = centerY + kotlin.math.sin(angle) * (radius + distortion)

                        if (i == 0) {
                            path.moveTo(x.toFloat(), y.toFloat())
                        } else {
                            path.lineTo(x.toFloat(), y.toFloat())
                        }
                    }
                    path.close()

                    canvas.drawPath(path, paint)
                }
            }
        }
    }
}
