package com.lightscout.lightlabs.liquidglass.opengl

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * OpenGL ES renderer for liquid glass effects. Handles rendering pipeline, texture management, and
 * shader execution.
 */
class LiquidGlassRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private var shaderManager: ShaderManager? = null
    private var backgroundTexture: Int = 0
    private var frameBuffer: Int = 0
    private var tempTexture: Int = 0

    private var vertexBuffer: FloatBuffer? = null
    private var indexBuffer: ByteBuffer? = null

    private var surfaceWidth = 0
    private var surfaceHeight = 0

    private val TAG = "LiquidGlassRenderer"

    private val quadVertices =
            floatArrayOf(
                    -1.0f,
                    1.0f,
                    0.0f,
                    1.0f,
                    -1.0f,
                    -1.0f,
                    0.0f,
                    0.0f,
                    1.0f,
                    -1.0f,
                    1.0f,
                    0.0f,
                    1.0f,
                    1.0f,
                    1.0f,
                    1.0f
            )

    private val quadIndices = byteArrayOf(0, 1, 2, 0, 2, 3)

    data class ShaderParams(
            val blurRadius: Float = 10.0f,
            val refractionStrength: Float = 0.5f,
            val chromaticAberration: Float = 1.0f,
            val glassThickness: Float = 0.2f,
            val glassColor: FloatArray = floatArrayOf(1.0f, 1.0f, 1.0f)
    )

    private var currentParams = ShaderParams()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        shaderManager = ShaderManager(context)
        initializeGeometry()

        Log.d(TAG, "Surface created, OpenGL initialized")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        surfaceWidth = width
        surfaceHeight = height

        createFrameBuffer(width, height)

        Log.d(TAG, "Surface changed: ${width}x${height}")
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        if (backgroundTexture != 0) {
            renderLiquidGlass()
        }
    }

    /** Update background texture from bitmap */
    fun updateBackgroundTexture(bitmap: Bitmap) {
        if (backgroundTexture == 0) {
            backgroundTexture = createTextureFromBitmap(bitmap)
        } else {
            updateTextureFromBitmap(backgroundTexture, bitmap)
        }
    }

    /** Update shader parameters */
    fun updateShaderParams(params: ShaderParams) {
        currentParams = params
    }

    /** Render liquid glass effect */
    private fun renderLiquidGlass() {
        val shaderManager = this.shaderManager ?: return

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backgroundTexture)
        val uniforms =
                mapOf(
                        "u_texture" to 0,
                        "u_resolution" to
                                floatArrayOf(surfaceWidth.toFloat(), surfaceHeight.toFloat()),
                        "u_time" to (System.currentTimeMillis() / 1000.0f),
                        "u_refractionStrength" to currentParams.refractionStrength,
                        "u_chromaticAberration" to currentParams.chromaticAberration,
                        "u_glassThickness" to currentParams.glassThickness,
                        "u_glassColor" to currentParams.glassColor
                )

        if (shaderManager.useShader(ShaderManager.ShaderType.LIQUID_GLASS, uniforms)) {
            drawQuad()
        }
    }

    /** Apply blur effect to current texture */
    fun applyBlur(type: ShaderManager.ShaderType, radius: Float) {
        val shaderManager = this.shaderManager ?: return

        if (type == ShaderManager.ShaderType.GAUSSIAN_BLUR) {
            applyGaussianBlur(radius)
        } else {
            applySinglePassBlur(type, radius)
        }
    }

    /** Apply two-pass Gaussian blur */
    private fun applyGaussianBlur(radius: Float) {
        val shaderManager = this.shaderManager ?: return

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backgroundTexture)

        val horizontalUniforms =
                mapOf(
                        "u_texture" to 0,
                        "u_resolution" to
                                floatArrayOf(surfaceWidth.toFloat(), surfaceHeight.toFloat()),
                        "u_direction" to floatArrayOf(1.0f, 0.0f),
                        "u_blurRadius" to radius
                )

        if (shaderManager.useShader(ShaderManager.ShaderType.GAUSSIAN_BLUR, horizontalUniforms)) {
            drawQuad()
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tempTexture)

        val verticalUniforms =
                mapOf(
                        "u_texture" to 0,
                        "u_resolution" to
                                floatArrayOf(surfaceWidth.toFloat(), surfaceHeight.toFloat()),
                        "u_direction" to floatArrayOf(0.0f, 1.0f),
                        "u_blurRadius" to radius
                )

        if (shaderManager.useShader(ShaderManager.ShaderType.GAUSSIAN_BLUR, verticalUniforms)) {
            drawQuad()
        }
    }

    /** Apply single-pass blur */
    private fun applySinglePassBlur(type: ShaderManager.ShaderType, radius: Float) {
        val shaderManager = this.shaderManager ?: return

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backgroundTexture)

        val uniforms =
                mapOf(
                        "u_texture" to 0,
                        "u_resolution" to
                                floatArrayOf(surfaceWidth.toFloat(), surfaceHeight.toFloat()),
                        "u_blurRadius" to radius
                )

        if (shaderManager.useShader(type, uniforms)) {
            drawQuad()
        }
    }

    /** Initialize quad geometry */
    private fun initializeGeometry() {
        val vbb = ByteBuffer.allocateDirect(quadVertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        vertexBuffer = vbb.asFloatBuffer()
        vertexBuffer?.put(quadVertices)
        vertexBuffer?.position(0)

        indexBuffer = ByteBuffer.allocateDirect(quadIndices.size)
        indexBuffer?.put(quadIndices)
        indexBuffer?.position(0)
    }

    /** Draw fullscreen quad */
    private fun drawQuad() {
        val vertexBuffer = this.vertexBuffer ?: return
        val indexBuffer = this.indexBuffer ?: return

        // Position attribute
        GLES20.glEnableVertexAttribArray(0)
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 4 * 4, vertexBuffer)

        // Texture coordinate attribute
        vertexBuffer.position(2)
        GLES20.glEnableVertexAttribArray(1)
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 4 * 4, vertexBuffer)
        vertexBuffer.position(0)

        // Draw
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_BYTE, indexBuffer)

        // Disable attributes
        GLES20.glDisableVertexAttribArray(0)
        GLES20.glDisableVertexAttribArray(1)
    }

    /** Create texture from bitmap */
    private fun createTextureFromBitmap(bitmap: Bitmap): Int {
        val textureIds = IntArray(1)
        GLES20.glGenTextures(1, textureIds, 0)

        val textureId = textureIds[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)

        // Set texture parameters
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE
        )

        // Upload bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        return textureId
    }

    /** Update existing texture with new bitmap */
    private fun updateTextureFromBitmap(textureId: Int, bitmap: Bitmap) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, bitmap)
    }

    /** Create framebuffer for multi-pass rendering */
    private fun createFrameBuffer(width: Int, height: Int) {
        // Create framebuffer
        val frameBuffers = IntArray(1)
        GLES20.glGenFramebuffers(1, frameBuffers, 0)
        frameBuffer = frameBuffers[0]

        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        tempTexture = textures[0]

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tempTexture)
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_RGBA,
                width,
                height,
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                null
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        // Attach texture to framebuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer)
        GLES20.glFramebufferTexture2D(
                GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                tempTexture,
                0
        )

        // Check framebuffer status
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            Log.e(TAG, "Framebuffer not complete: $status")
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    /** Cleanup resources */
    fun cleanup() {
        shaderManager?.cleanup()

        if (backgroundTexture != 0) {
            GLES20.glDeleteTextures(1, intArrayOf(backgroundTexture), 0)
        }
        if (tempTexture != 0) {
            GLES20.glDeleteTextures(1, intArrayOf(tempTexture), 0)
        }
        if (frameBuffer != 0) {
            GLES20.glDeleteFramebuffers(1, intArrayOf(frameBuffer), 0)
        }
    }
}
