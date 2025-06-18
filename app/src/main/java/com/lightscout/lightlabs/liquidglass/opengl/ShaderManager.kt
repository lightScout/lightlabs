package com.lightscout.lightlabs.liquidglass.opengl

import android.content.Context
import android.opengl.GLES20
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Manages OpenGL ES shaders for liquid glass effects. Handles loading, compiling, and managing
 * shader programs.
 */
class ShaderManager(private val context: Context) {

    private val shaderPrograms = mutableMapOf<String, Int>()
    private val TAG = "ShaderManager"

    enum class ShaderType {
        FAST_BLUR,
        GAUSSIAN_BLUR,
        LIQUID_GLASS
    }

    init {
        loadAllShaders()
    }

    /** Load all shader programs */
    private fun loadAllShaders() {
        try {
            shaderPrograms[ShaderType.FAST_BLUR.name] =
                    createShaderProgram(
                            loadShaderFromAssets("shaders/vertex_shader.glsl"),
                            loadShaderFromAssets("shaders/fast_blur.glsl")
                    )

            shaderPrograms[ShaderType.GAUSSIAN_BLUR.name] =
                    createShaderProgram(
                            loadShaderFromAssets("shaders/vertex_shader.glsl"),
                            loadShaderFromAssets("shaders/gaussian_blur.glsl")
                    )

            shaderPrograms[ShaderType.LIQUID_GLASS.name] =
                    createShaderProgram(
                            loadShaderFromAssets("shaders/vertex_shader.glsl"),
                            loadShaderFromAssets("shaders/liquid_glass.glsl")
                    )

            Log.d(TAG, "All shaders loaded successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load shaders", e)
        }
    }

    /** Get shader program by type */
    fun getShaderProgram(type: ShaderType): Int? {
        return shaderPrograms[type.name]
    }

    /** Use shader program and set common uniforms */
    fun useShader(type: ShaderType, uniforms: Map<String, Any> = emptyMap()): Boolean {
        val program = getShaderProgram(type) ?: return false

        GLES20.glUseProgram(program)
        checkGLError("useProgram")

        // Set uniforms
        uniforms.forEach { (name, value) ->
            val location = GLES20.glGetUniformLocation(program, name)
            if (location != -1) {
                when (value) {
                    is Float -> GLES20.glUniform1f(location, value)
                    is Int -> GLES20.glUniform1i(location, value)
                    is FloatArray -> {
                        when (value.size) {
                            2 -> GLES20.glUniform2fv(location, 1, value, 0)
                            3 -> GLES20.glUniform3fv(location, 1, value, 0)
                            4 -> GLES20.glUniform4fv(location, 1, value, 0)
                        }
                    }
                }
            }
        }

        return true
    }

    /** Load shader source from assets */
    private fun loadShaderFromAssets(fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val shaderSource = reader.use { it.readText() }
        return shaderSource
    }

    /** Create and compile a shader */
    private fun compileShader(type: Int, source: String): Int {
        val shader = GLES20.glCreateShader(type)
        if (shader == 0) {
            throw RuntimeException("Failed to create shader")
        }

        GLES20.glShaderSource(shader, source)
        GLES20.glCompileShader(shader)

        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

        if (compileStatus[0] == 0) {
            val error = GLES20.glGetShaderInfoLog(shader)
            GLES20.glDeleteShader(shader)
            throw RuntimeException("Shader compilation failed: $error")
        }

        return shader
    }

    /** Create shader program from vertex and fragment shaders */
    private fun createShaderProgram(vertexSource: String, fragmentSource: String): Int {
        val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        val fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)

        val program = GLES20.glCreateProgram()
        if (program == 0) {
            throw RuntimeException("Failed to create shader program")
        }

        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)

        if (linkStatus[0] == 0) {
            val error = GLES20.glGetProgramInfoLog(program)
            GLES20.glDeleteProgram(program)
            throw RuntimeException("Shader program linking failed: $error")
        }

        // Clean up individual shaders
        GLES20.glDeleteShader(vertexShader)
        GLES20.glDeleteShader(fragmentShader)

        return program
    }

    /** Check for OpenGL errors */
    private fun checkGLError(operation: String) {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            Log.e(TAG, "$operation: glError $error")
        }
    }

    /** Cleanup resources */
    fun cleanup() {
        shaderPrograms.values.forEach { program -> GLES20.glDeleteProgram(program) }
        shaderPrograms.clear()
    }
}
