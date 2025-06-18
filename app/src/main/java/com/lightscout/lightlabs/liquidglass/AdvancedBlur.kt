package com.lightscout.lightlabs.liquidglass

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.math.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * High-performance software blur implementation that mimics RenderScript quality without the
 * dependency issues. Uses optimized algorithms for real-time performance.
 */
class AdvancedBlur {

    /** Quality levels for different performance requirements */
    enum class BlurQuality {
        FAST, // Simple box blur - fastest
        STANDARD, // Gaussian approximation - balanced
        PREMIUM, // Multi-pass Gaussian - highest quality
        LIQUID // Special liquid glass blur with effects
    }

    /** Apply advanced blur to a bitmap with specified parameters */
    suspend fun blurBitmap(
            inputBitmap: Bitmap,
            radius: Float,
            quality: BlurQuality = BlurQuality.STANDARD
    ): Bitmap =
            withContext(Dispatchers.Default) {
                val width = inputBitmap.width
                val height = inputBitmap.height
                val outputBitmap = inputBitmap.copy(inputBitmap.config, true)

                when (quality) {
                    BlurQuality.FAST -> applyBoxBlur(outputBitmap, radius.toInt())
                    BlurQuality.STANDARD -> applyStackBlur(outputBitmap, radius.toInt())
                    BlurQuality.PREMIUM -> applyMultiPassGaussian(outputBitmap, radius)
                    BlurQuality.LIQUID -> applyLiquidGlassBlur(outputBitmap, radius)
                }

                outputBitmap
            }

    /** Fast box blur implementation */
    private fun applyBoxBlur(bitmap: Bitmap, radius: Int) {
        if (radius <= 0) return

        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        // Horizontal pass
        boxBlurHorizontal(pixels, width, height, radius)
        // Vertical pass
        boxBlurVertical(pixels, width, height, radius)

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }

    /** High-quality stack blur (approximates Gaussian) */
    private fun applyStackBlur(bitmap: Bitmap, radius: Int) {
        if (radius <= 0) return

        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        stackBlurJob(pixels, width, height, radius, 0, height)

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }

    /** Multi-pass Gaussian for premium quality */
    private fun applyMultiPassGaussian(bitmap: Bitmap, radius: Float) {
        if (radius <= 0) return

        // Multiple smaller passes for better quality
        val passes =
                when {
                    radius < 5 -> 1
                    radius < 15 -> 2
                    else -> 3
                }

        val passRadius = (radius / passes).toInt().coerceAtLeast(1)

        repeat(passes) { applyStackBlur(bitmap, passRadius) }
    }

    /** Special liquid glass blur with depth and chromatic effects */
    private fun applyLiquidGlassBlur(bitmap: Bitmap, radius: Float) {
        if (radius <= 0) return

        // Base blur
        applyStackBlur(bitmap, radius.toInt())

        // Apply glass-like effects
        val canvas = Canvas(bitmap)
        val paint =
                Paint().apply {
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
                    alpha = 51 // 20% opacity
                }

        // Create subtle color shifts for glass refraction effect
        val shiftBitmap = bitmap.copy(bitmap.config, true)
        applyColorShift(shiftBitmap, 0.02f)

        canvas.drawBitmap(shiftBitmap, 2f, 1f, paint)
        shiftBitmap.recycle()
    }

    /** Apply subtle color shift for glass refraction simulation */
    private fun applyColorShift(bitmap: Bitmap, intensity: Float) {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in pixels.indices) {
            val pixel = pixels[i]
            val a = (pixel shr 24) and 0xFF
            val r = ((pixel shr 16) and 0xFF)
            val g = ((pixel shr 8) and 0xFF)
            val b = (pixel and 0xFF)

            // Slight red/blue shift for chromatic aberration
            val newR = (r * (1f + intensity)).toInt().coerceIn(0, 255)
            val newB = (b * (1f - intensity * 0.5f)).toInt().coerceIn(0, 255)

            pixels[i] = (a shl 24) or (newR shl 16) or (g shl 8) or newB
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }

    /** Optimized horizontal box blur */
    private fun boxBlurHorizontal(pixels: IntArray, width: Int, height: Int, radius: Int) {
        val diameter = radius * 2 + 1

        for (y in 0 until height) {
            var redSum = 0
            var greenSum = 0
            var blueSum = 0
            var alphaSum = 0

            val rowStart = y * width

            // Initialize window
            for (x in -radius..radius) {
                val index = rowStart + x.coerceIn(0, width - 1)
                val pixel = pixels[index]
                redSum += (pixel shr 16) and 0xFF
                greenSum += (pixel shr 8) and 0xFF
                blueSum += pixel and 0xFF
                alphaSum += (pixel shr 24) and 0xFF
            }

            // Slide window
            for (x in 0 until width) {
                val index = rowStart + x
                pixels[index] =
                        (alphaSum / diameter shl 24) or
                                (redSum / diameter shl 16) or
                                (greenSum / diameter shl 8) or
                                (blueSum / diameter)

                // Update sums for next position
                if (x + radius + 1 < width) {
                    val addIndex = rowStart + x + radius + 1
                    val addPixel = pixels[addIndex]
                    redSum += (addPixel shr 16) and 0xFF
                    greenSum += (addPixel shr 8) and 0xFF
                    blueSum += addPixel and 0xFF
                    alphaSum += (addPixel shr 24) and 0xFF
                }

                if (x - radius >= 0) {
                    val removeIndex = rowStart + x - radius
                    val removePixel = pixels[removeIndex]
                    redSum -= (removePixel shr 16) and 0xFF
                    greenSum -= (removePixel shr 8) and 0xFF
                    blueSum -= removePixel and 0xFF
                    alphaSum -= (removePixel shr 24) and 0xFF
                }
            }
        }
    }

    /** Optimized vertical box blur */
    private fun boxBlurVertical(pixels: IntArray, width: Int, height: Int, radius: Int) {
        val diameter = radius * 2 + 1

        for (x in 0 until width) {
            var redSum = 0
            var greenSum = 0
            var blueSum = 0
            var alphaSum = 0

            // Initialize window
            for (y in -radius..radius) {
                val index = y.coerceIn(0, height - 1) * width + x
                val pixel = pixels[index]
                redSum += (pixel shr 16) and 0xFF
                greenSum += (pixel shr 8) and 0xFF
                blueSum += pixel and 0xFF
                alphaSum += (pixel shr 24) and 0xFF
            }

            // Slide window
            for (y in 0 until height) {
                val index = y * width + x
                pixels[index] =
                        (alphaSum / diameter shl 24) or
                                (redSum / diameter shl 16) or
                                (greenSum / diameter shl 8) or
                                (blueSum / diameter)

                // Update sums for next position
                if (y + radius + 1 < height) {
                    val addIndex = (y + radius + 1) * width + x
                    val addPixel = pixels[addIndex]
                    redSum += (addPixel shr 16) and 0xFF
                    greenSum += (addPixel shr 8) and 0xFF
                    blueSum += addPixel and 0xFF
                    alphaSum += (addPixel shr 24) and 0xFF
                }

                if (y - radius >= 0) {
                    val removeIndex = (y - radius) * width + x
                    val removePixel = pixels[removeIndex]
                    redSum -= (removePixel shr 16) and 0xFF
                    greenSum -= (removePixel shr 8) and 0xFF
                    blueSum -= removePixel and 0xFF
                    alphaSum -= (removePixel shr 24) and 0xFF
                }
            }
        }
    }

    /** Stack blur implementation for high quality */
    private fun stackBlurJob(
            pixels: IntArray,
            width: Int,
            height: Int,
            radius: Int,
            startY: Int,
            endY: Int
    ) {
        val div = radius + radius + 1
        val w4 = width shl 2
        val widthMinus1 = width - 1
        val heightMinus1 = height - 1
        val radiusPlus1 = radius + 1

        val stack = IntArray(div)
        var stackPointer: Int
        var stackStart: Int
        var sir: Int
        var rbs: Int
        val r1 = radius + 1
        var routSum: Int
        var goutSum: Int
        var boutSum: Int
        var aoutSum: Int
        var rinSum: Int
        var ginSum: Int
        var binSum: Int
        var ainSum: Int

        for (y in startY until endY) {
            ainSum = 0
            binSum = ainSum
            ginSum = binSum
            rinSum = ginSum
            aoutSum = rinSum
            boutSum = aoutSum
            goutSum = boutSum
            routSum = goutSum

            val yp = y * width
            for (i in -radius..radius) {
                val yi = yp + (if (i > widthMinus1) widthMinus1 else if (i < 0) 0 else i)
                sir = pixels[yi]

                val rsi = radius - abs(i)
                stack[rsi] = sir

                routSum += ((sir and 0xff0000) shr 16) * rsi
                goutSum += ((sir and 0x00ff00) shr 8) * rsi
                boutSum += (sir and 0x0000ff) * rsi
                aoutSum += ((sir and -0x1000000) ushr 24) * rsi

                if (i > 0) {
                    rinSum += (sir and 0xff0000) shr 16
                    ginSum += (sir and 0x00ff00) shr 8
                    binSum += sir and 0x0000ff
                    ainSum += (sir and -0x1000000) ushr 24
                }
            }

            stackPointer = radius

            for (x in 0 until width) {
                pixels[yp + x] =
                        (aoutSum * 0x1000000 / div).toInt() and
                                -0x1000000 or
                                ((routSum / div) shl 16) or
                                ((goutSum / div) shl 8) or
                                (boutSum / div)

                aoutSum -= ainSum
                routSum -= rinSum
                goutSum -= ginSum
                boutSum -= binSum

                stackStart = stackPointer - radius + div
                sir = stack[stackStart % div]

                aoutSum -= (sir and -0x1000000) ushr 24
                routSum -= (sir and 0xff0000) shr 16
                goutSum -= (sir and 0x00ff00) shr 8
                boutSum -= sir and 0x0000ff

                if (y == 0) {
                    val minX = if (x + radius + 1 < widthMinus1) x + radius + 1 else widthMinus1
                    sir = pixels[yp + minX]
                }

                stack[stackStart % div] = sir

                ainSum += (sir and -0x1000000) ushr 24
                rinSum += (sir and 0xff0000) shr 16
                ginSum += (sir and 0x00ff00) shr 8
                binSum += sir and 0x0000ff

                aoutSum += ainSum
                routSum += rinSum
                goutSum += ginSum
                boutSum += binSum

                stackPointer = (stackPointer + 1) % div
                sir = stack[stackPointer % div]

                ainSum -= (sir and -0x1000000) ushr 24
                rinSum -= (sir and 0xff0000) shr 16
                ginSum -= (sir and 0x00ff00) shr 8
                binSum -= sir and 0x0000ff
            }
        }
    }
}

/** Composable function to remember AdvancedBlur instance */
@Composable
fun rememberAdvancedBlur(): AdvancedBlur {
    return remember { AdvancedBlur() }
}
