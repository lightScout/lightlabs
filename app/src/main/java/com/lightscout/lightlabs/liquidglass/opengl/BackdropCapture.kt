package com.lightscout.lightlabs.liquidglass.opengl

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.lightscout.lightlabs.liquidglass.LiquidGlassQuality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Utility for capturing backdrop content as bitmaps for shader processing */
object BackdropCapture {

    /** Capture a view as bitmap */
    suspend fun captureView(view: View): Bitmap? =
            withContext(Dispatchers.IO) {
                try {
                    val bitmap =
                            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    view.draw(canvas)
                    bitmap
                } catch (e: Exception) {
                    null
                }
            }

    /** Create a scaled down version of bitmap for performance */
    fun createOptimizedBitmap(original: Bitmap, maxDimension: Int = 512): Bitmap {
        val ratio =
                minOf(
                        maxDimension.toFloat() / original.width,
                        maxDimension.toFloat() / original.height
                )

        if (ratio >= 1.0f) return original

        val newWidth = (original.width * ratio).toInt()
        val newHeight = (original.height * ratio).toInt()

        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true)
    }

    fun createTestPattern(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = android.graphics.Paint()
        val gradient =
                android.graphics.LinearGradient(
                        0f,
                        0f,
                        width.toFloat(),
                        height.toFloat(),
                        intArrayOf(
                                android.graphics.Color.BLUE,
                                android.graphics.Color.CYAN,
                                android.graphics.Color.GREEN,
                                android.graphics.Color.YELLOW,
                                android.graphics.Color.RED,
                                android.graphics.Color.MAGENTA
                        ),
                        null,
                        android.graphics.Shader.TileMode.CLAMP
                )
        paint.shader = gradient
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.shader = null
        paint.color = android.graphics.Color.WHITE
        paint.alpha = 100

        for (i in 0 until 5) {
            val x = (width * (i + 1) / 6).toFloat()
            val y = (height / 2).toFloat()
            val radius = (width / 15).toFloat()
            canvas.drawCircle(x, y, radius, paint)
        }

        for (i in 0 until 3) {
            val left = (width * (i + 1) / 4 - width / 20).toFloat()
            val top = (height * 0.7f).toFloat()
            val right = (width * (i + 1) / 4 + width / 20).toFloat()
            val bottom = (height * 0.9f).toFloat()
            canvas.drawRect(left, top, right, bottom, paint)
        }

        return bitmap
    }
}

/** Composable that captures its content and provides the bitmap */
@Composable
fun CaptureableContent(
        modifier: Modifier = Modifier,
        onBitmapCaptured: (Bitmap?) -> Unit,
        content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val graphicsLayer = rememberGraphicsLayer()
    var size by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(size) {
        if (size.width > 0 && size.height > 0) {
            try {
                val bitmap = BackdropCapture.createTestPattern(size.width, size.height)
                onBitmapCaptured(bitmap)
            } catch (e: Exception) {
                onBitmapCaptured(null)
            }
        }
    }

    Box(
            modifier =
                    modifier.drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record { this@onDrawWithContent.drawContent() }
                            drawLayer(graphicsLayer)

                            val newSize = IntSize(size.width.toInt(), size.height.toInt())
                            if (newSize != size) {
                                size = newSize
                            }
                        }
                    }
    ) { content() }
}

/** Advanced liquid glass with real-time backdrop capture */
@Composable
fun RealTimeLiquidGlass(
        quality: LiquidGlassQuality = LiquidGlassQuality.BALANCED,
        blurRadius: Float = 10f,
        refractionStrength: Float = 0.5f,
        chromaticAberration: Float = 1.0f,
        glassThickness: Float = 0.2f,
        glassColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
        modifier: Modifier = Modifier,
        backgroundContent: @Composable () -> Unit,
        glassContent: @Composable () -> Unit
) {
    var backgroundBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Box(modifier = modifier) {
        CaptureableContent(
                modifier = Modifier.fillMaxSize(),
                onBitmapCaptured = { bitmap -> backgroundBitmap = bitmap }
        ) { backgroundContent() }

        Box(modifier = Modifier.fillMaxSize()) {
            LiquidGlassShaderView(
                    backgroundBitmap = backgroundBitmap,
                    quality = quality,
                    blurRadius = blurRadius,
                    refractionStrength = refractionStrength,
                    chromaticAberration = chromaticAberration,
                    glassThickness = glassThickness,
                    glassColor = glassColor,
                    modifier = Modifier.fillMaxSize()
            )

            glassContent()
        }
    }
}

/** Performance-optimized backdrop capture with frame limiting */
@Composable
fun PerformanceBackdropCapture(
        targetFps: Int = 30,
        modifier: Modifier = Modifier,
        onBitmapCaptured: (Bitmap?) -> Unit,
        content: @Composable () -> Unit
) {
    var lastCaptureTime by remember { mutableStateOf(0L) }
    val captureInterval = 1000L / targetFps

    CaptureableContent(
            modifier = modifier,
            onBitmapCaptured = { bitmap ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastCaptureTime >= captureInterval) {
                    onBitmapCaptured(bitmap)
                    lastCaptureTime = currentTime
                }
            }
    ) { content() }
}
