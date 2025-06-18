package com.lightscout.lightlabs.liquidglass.opengl

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.lightscout.lightlabs.liquidglass.LiquidGlassQuality

/** OpenGL surface view for liquid glass effects */
class LiquidGlassSurfaceView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    private val renderer: LiquidGlassRenderer = LiquidGlassRenderer(context)

    init {
        setEGLContextClientVersion(2)
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun updateBackground(bitmap: Bitmap) {
        queueEvent {
            renderer.updateBackgroundTexture(bitmap)
            requestRender()
        }
    }

    fun updateShaderParams(params: LiquidGlassRenderer.ShaderParams) {
        queueEvent {
            renderer.updateShaderParams(params)
            requestRender()
        }
    }

    fun applyBlur(type: ShaderManager.ShaderType, radius: Float) {
        queueEvent {
            renderer.applyBlur(type, radius)
            requestRender()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        queueEvent { renderer.cleanup() }
    }
}

/** Composable wrapper for liquid glass effects using OpenGL ES shaders */
@Composable
fun LiquidGlassShaderView(
        backgroundBitmap: Bitmap?,
        quality: LiquidGlassQuality = LiquidGlassQuality.BALANCED,
        blurRadius: Float = 10f,
        refractionStrength: Float = 0.5f,
        chromaticAberration: Float = 1.0f,
        glassThickness: Float = 0.2f,
        glassColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
        modifier: Modifier = Modifier,
        onViewCreated: ((LiquidGlassSurfaceView) -> Unit)? = null
) {
    val context = LocalContext.current
    var surfaceView by remember { mutableStateOf<LiquidGlassSurfaceView?>(null) }

    // Convert quality to shader type
    val shaderType =
            when (quality) {
                LiquidGlassQuality.PERFORMANCE -> ShaderManager.ShaderType.FAST_BLUR
                LiquidGlassQuality.BALANCED -> ShaderManager.ShaderType.GAUSSIAN_BLUR
                LiquidGlassQuality.PREMIUM -> ShaderManager.ShaderType.LIQUID_GLASS
            }

    // Update shader parameters when values change
    LaunchedEffect(
            quality,
            blurRadius,
            refractionStrength,
            chromaticAberration,
            glassThickness,
            glassColor
    ) {
        surfaceView?.let { view ->
            val params =
                    LiquidGlassRenderer.ShaderParams(
                            blurRadius = blurRadius,
                            refractionStrength = refractionStrength,
                            chromaticAberration = chromaticAberration,
                            glassThickness = glassThickness,
                            glassColor =
                                    floatArrayOf(glassColor.red, glassColor.green, glassColor.blue)
                    )
            view.updateShaderParams(params)

            // Apply blur if not using liquid glass shader
            if (shaderType != ShaderManager.ShaderType.LIQUID_GLASS) {
                view.applyBlur(shaderType, blurRadius)
            }
        }
    }

    // Update background when bitmap changes
    LaunchedEffect(backgroundBitmap) {
        backgroundBitmap?.let { bitmap -> surfaceView?.updateBackground(bitmap) }
    }

    AndroidView(
            factory = { context ->
                LiquidGlassSurfaceView(context).also { view ->
                    surfaceView = view
                    onViewCreated?.invoke(view)
                }
            },
            modifier = modifier
    )
}

/** Advanced liquid glass composable with automatic backdrop capture */
@Composable
fun AutoLiquidGlassShader(
        content: @Composable () -> Unit,
        quality: LiquidGlassQuality = LiquidGlassQuality.BALANCED,
        blurRadius: Float = 10f,
        refractionStrength: Float = 0.5f,
        chromaticAberration: Float = 1.0f,
        glassThickness: Float = 0.2f,
        glassColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
        modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var backgroundBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var surfaceView by remember { mutableStateOf<LiquidGlassSurfaceView?>(null) }

    Box(modifier = modifier) {
        // Background content
        Box(modifier = Modifier.fillMaxSize()) { content() }

        // Glass overlay
        LiquidGlassShaderView(
                backgroundBitmap = backgroundBitmap,
                quality = quality,
                blurRadius = blurRadius,
                refractionStrength = refractionStrength,
                chromaticAberration = chromaticAberration,
                glassThickness = glassThickness,
                glassColor = glassColor,
                modifier = Modifier.fillMaxSize(),
                onViewCreated = { view -> surfaceView = view }
        )
    }
}

/** Performance monitoring for shader effects */
@Composable
fun rememberShaderPerformance(): ShaderPerformanceState {
    return remember { ShaderPerformanceState() }
}

class ShaderPerformanceState {
    var lastFrameTime by mutableStateOf(0L)
    var averageFrameTime by mutableStateOf(0f)
    var fps by mutableStateOf(0f)

    private val frameTimes = mutableListOf<Long>()
    private var lastUpdateTime = System.currentTimeMillis()

    fun recordFrame() {
        val currentTime = System.currentTimeMillis()
        val frameTime = currentTime - lastUpdateTime
        lastUpdateTime = currentTime

        frameTimes.add(frameTime)
        if (frameTimes.size > 60) {
            frameTimes.removeFirst()
        }

        lastFrameTime = frameTime
        averageFrameTime = frameTimes.average().toFloat()
        fps = if (averageFrameTime > 0) 1000f / averageFrameTime else 0f
    }
}
