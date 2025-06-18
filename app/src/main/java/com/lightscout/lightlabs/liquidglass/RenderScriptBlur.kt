package com.lightscout.lightlabs.liquidglass

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.runBlocking

/**
 * High-performance blur operations for liquid glass effects. Uses advanced software algorithms that
 * provide RenderScript-like quality without compatibility issues.
 */
class RenderScriptBlur(private val context: Context) {

    private val advancedBlur = AdvancedBlur()

    /** Quality levels for different performance requirements */
    enum class BlurQuality {
        FAST, // Box blur - fastest
        STANDARD, // Gaussian blur - balanced
        PREMIUM, // Advanced blur with effects
        LIQUID // Special liquid glass blur with depth
    }

    /** Apply blur to a bitmap with specified parameters */
    fun blurBitmap(
            inputBitmap: Bitmap,
            radius: Float,
            quality: BlurQuality = BlurQuality.STANDARD
    ): Bitmap? {
        return try {
            runBlocking {
                val advancedQuality =
                        when (quality) {
                            BlurQuality.FAST -> AdvancedBlur.BlurQuality.FAST
                            BlurQuality.STANDARD -> AdvancedBlur.BlurQuality.STANDARD
                            BlurQuality.PREMIUM -> AdvancedBlur.BlurQuality.PREMIUM
                            BlurQuality.LIQUID -> AdvancedBlur.BlurQuality.LIQUID
                        }
                advancedBlur.blurBitmap(inputBitmap, radius, advancedQuality)
            }
        } catch (e: Exception) {
            // Return original bitmap on error
            inputBitmap.copy(inputBitmap.config, false)
        }
    }

    /** Create a blurred version of bitmap for glass backdrop effect */
    fun createGlassBackdrop(
            inputBitmap: Bitmap,
            blurRadius: Float,
            intensity: Float = 1.0f
    ): Bitmap? {
        val blurred = blurBitmap(inputBitmap, blurRadius, BlurQuality.LIQUID)

        return blurred?.let { bitmap ->
            // Apply glass-like effects
            val canvas = android.graphics.Canvas(bitmap)
            val paint =
                    android.graphics.Paint().apply {
                        colorFilter =
                                android.graphics.ColorMatrixColorFilter(
                                        android.graphics.ColorMatrix().apply {
                                            // Increase contrast and saturation for glass effect
                                            setSaturation(1.0f + intensity * 0.3f)
                                            setScale(
                                                    1.0f + intensity * 0.1f,
                                                    1.0f + intensity * 0.1f,
                                                    1.0f + intensity * 0.1f,
                                                    1.0f
                                            )
                                        }
                                )
                        alpha = (255 * (0.7f + intensity * 0.3f)).toInt()
                    }

            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            bitmap
        }
    }

    /** Cleanup resources (no-op for software implementation) */
    fun destroy() {
        // No cleanup needed for software implementation
    }
}

/** Composable function to remember RenderScript blur instance */
@Composable
fun rememberRenderScriptBlur(): RenderScriptBlur {
    val context = LocalContext.current
    return remember { RenderScriptBlur(context) }
}

/** Enhanced blur utilities with high-performance software blur */
object EnhancedBlurUtils {

    /** Apply real backdrop blur effect using advanced software blur */
    fun applyBackdropBlur(
            bitmap: Bitmap,
            radius: Float,
            quality: RenderScriptBlur.BlurQuality = RenderScriptBlur.BlurQuality.STANDARD,
            blurInstance: RenderScriptBlur
    ): Bitmap? {
        return blurInstance.blurBitmap(bitmap, radius, quality)
    }

    /** Create optimized blur for liquid glass with depth effects */
    fun createLiquidGlassBlur(
            bitmap: Bitmap,
            radius: Float,
            depthIntensity: Float = 0.5f,
            blurInstance: RenderScriptBlur
    ): Bitmap? {
        // Use special liquid glass blur kernel
        return blurInstance.blurBitmap(bitmap, radius, RenderScriptBlur.BlurQuality.LIQUID)?.let {
                blurred ->
            // Apply additional depth effects
            blurInstance.createGlassBackdrop(blurred, radius * 0.3f, depthIntensity)
        }
    }
}
