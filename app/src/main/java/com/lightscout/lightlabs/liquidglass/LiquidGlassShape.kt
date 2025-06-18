package com.lightscout.lightlabs.liquidglass

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/**
 * Enhanced shape system for liquid glass components. Inspired by Flutter's liquid_shape
 * architecture.
 */
sealed class LiquidGlassShape {
    abstract fun createOutline(size: Size, density: Density): Outline
}

/** Rounded rectangle shape with enhanced corner handling */
class LiquidRoundedRectShape(
        private val cornerRadius: CornerSize = CornerSize(16.dp),
        private val smoothing: Float = 0.1f
) : LiquidGlassShape() {

    override fun createOutline(size: Size, density: Density): Outline {
        val radius = cornerRadius.toPx(size, density)

        return if (smoothing > 0f) {
            // Create smooth corners using Bézier curves
            Outline.Generic(createSmoothRoundedRect(size, radius, smoothing))
        } else {
            // Standard rounded rectangle
            Outline.Rounded(
                    androidx.compose.ui.geometry.RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            radiusX = radius,
                            radiusY = radius
                    )
            )
        }
    }

    private fun createSmoothRoundedRect(size: Size, radius: Float, smoothing: Float): Path {
        val path = Path()
        val smoothRadius = radius * (1f + smoothing)
        val controlPoint = radius * smoothing

        path.moveTo(radius, 0f)
        path.lineTo(size.width - radius, 0f)

        // Top-right corner with smooth transition
        path.cubicTo(size.width - controlPoint, 0f, size.width, controlPoint, size.width, radius)

        path.lineTo(size.width, size.height - radius)

        // Bottom-right corner
        path.cubicTo(
                size.width,
                size.height - controlPoint,
                size.width - controlPoint,
                size.height,
                size.width - radius,
                size.height
        )

        path.lineTo(radius, size.height)

        // Bottom-left corner
        path.cubicTo(
                controlPoint,
                size.height,
                0f,
                size.height - controlPoint,
                0f,
                size.height - radius
        )

        path.lineTo(0f, radius)

        // Top-left corner
        path.cubicTo(0f, controlPoint, controlPoint, 0f, radius, 0f)

        path.close()
        return path
    }
}

/** Pill/capsule shape for buttons and smaller components */
class LiquidPillShape : LiquidGlassShape() {
    override fun createOutline(size: Size, density: Density): Outline {
        val radius = minOf(size.width, size.height) / 2f

        return Outline.Rounded(
                androidx.compose.ui.geometry.RoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        radiusX = radius,
                        radiusY = radius
                )
        )
    }
}

/** Custom squircle shape for premium feel */
class LiquidPremiumShape(private val radius: CornerSize = CornerSize(16.dp)) : LiquidGlassShape() {

    override fun createOutline(size: Size, density: Density): Outline {
        val r = radius.toPx(size, density)
        return Outline.Rounded(
                androidx.compose.ui.geometry.RoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        radiusX = r,
                        radiusY = r
                )
        )
    }
}

/** Shape factory for creating appropriate shapes based on component type */
object LiquidGlassShapes {
    fun button(cornerRadius: CornerSize = CornerSize(12.dp)) = LiquidPillShape()

    fun card(cornerRadius: CornerSize = CornerSize(16.dp)) =
            LiquidRoundedRectShape(cornerRadius, smoothing = 0.15f)

    fun premium(cornerRadius: CornerSize = CornerSize(20.dp)) = LiquidPremiumShape(cornerRadius)

    fun navbar(cornerRadius: CornerSize = CornerSize(24.dp)) =
            LiquidRoundedRectShape(cornerRadius, smoothing = 0.2f)
}
