package com.lightscout.lightlabs.liquidglass

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Configuration settings for Liquid Glass effects. Enhanced architecture inspired by Flutter
 * liquid_glass_settings.
 */
data class LiquidGlassSettings(
        // === Material Properties ===
        /** The base color of the glass material. Will be blended with background content. */
        val baseColor: Color = Color.White.copy(alpha = 0.05f),

        /** The strength of the frost/noise effect */
        val frostStrength: Float = 0.1f,

        /** Intensity of specular highlights */
        val specularIntensity: Float = 0.3f,

        /** Noise texture strength for surface detail */
        val noiseStrength: Float = 0.05f,

        // === Geometry ===
        /** Border radius for the glass container */
        val borderRadius: Dp = 16.dp,

        /** Custom shape for the glass container */
        val shape: LiquidGlassShape? = null,

        // === Border & Edge Effects ===
        /** Width of the luminous bezel around the glass */
        val bezelWidth: Dp = 0.8.dp,

        /** Color of the bezel highlight */
        val bezelColor: Color = Color.White.copy(alpha = 0.2f),

        // === Optical Effects ===
        /** The intensity of the blur effect applied to background content */
        val blurRadius: Dp = 15.dp,

        /** Intensity of light refraction effects */
        val refractionIntensity: Float = 0.15f,

        /** Whether to enable dynamic lighting based on device orientation */
        val dynamicLighting: Boolean = true,

        // === Interaction ===
        /** Scale factor when touched/pressed */
        val touchScale: Float = 0.98f,

        /** Duration of touch animations in milliseconds */
        val animationDuration: Int = 200,

        /** Whether to enable gel-like touch and drag effects */
        val gelTouchEnabled: Boolean = true,

        // === Quality & Performance ===
        /** Rendering quality level */
        val quality: LiquidGlassQuality = LiquidGlassQuality.BALANCED
)

/** Quality presets for different performance requirements */
enum class LiquidGlassQuality {
    /** Minimal effects for maximum performance */
    PERFORMANCE,

    /** Balanced quality and performance */
    BALANCED,

    /** Maximum visual quality */
    PREMIUM
}

/** Preset configurations inspired by common use cases */
object LiquidGlassPresets {
    /** Clean, minimal glass for buttons */
    val button =
            LiquidGlassSettings(
                    baseColor = Color.White.copy(alpha = 0.08f),
                    specularIntensity = 0.4f,
                    borderRadius = 12.dp,
                    bezelWidth = 0.5.dp,
                    quality = LiquidGlassQuality.BALANCED
            )

    /** Rich glass effect for cards */
    val card =
            LiquidGlassSettings(
                    baseColor = Color.White.copy(alpha = 0.06f),
                    frostStrength = 0.15f,
                    specularIntensity = 0.3f,
                    borderRadius = 16.dp,
                    bezelWidth = 1.dp,
                    quality = LiquidGlassQuality.PREMIUM
            )

    /** Subtle glass for navigation elements */
    val navigation =
            LiquidGlassSettings(
                    baseColor = Color.White.copy(alpha = 0.04f),
                    specularIntensity = 0.2f,
                    borderRadius = 24.dp,
                    bezelWidth = 0.8.dp,
                    quality = LiquidGlassQuality.BALANCED
            )

    /** High-impact glass for showcase elements */
    val hero =
            LiquidGlassSettings(
                    baseColor = Color.White.copy(alpha = 0.1f),
                    frostStrength = 0.2f,
                    specularIntensity = 0.5f,
                    borderRadius = 20.dp,
                    bezelWidth = 1.5.dp,
                    bezelColor = Color.White.copy(alpha = 0.3f),
                    quality = LiquidGlassQuality.PREMIUM
            )
}
