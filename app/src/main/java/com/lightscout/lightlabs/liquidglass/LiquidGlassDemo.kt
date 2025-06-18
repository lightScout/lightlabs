package com.lightscout.lightlabs.liquidglass

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lightscout.lightlabs.liquidglass.opengl.*

/** Clean, organized demo showcasing Liquid Glass components */
@Composable
fun LiquidGlassDemo() {
    var selectedQuality by remember { mutableStateOf(LiquidGlassQuality.BALANCED) }
    var showSettings by remember { mutableStateOf(false) }

    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(
                brush =
                Brush.radialGradient(
                    colors =
                    listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF3B82F6)
                            .copy(alpha = 0.8f),
                        Color(0xFF8B5CF6)
                            .copy(alpha = 0.6f)
                    ),
                    radius = 1200f
                )
            )
    ) {
        // Subtle background pattern
        BackgroundPattern()

        // Main content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            item { HeaderSection() }

            // Quality selector
            item {
                QualitySelector(
                    selectedQuality = selectedQuality,
                    onQualitySelected = { selectedQuality = it }
                )
            }

            // Shader showcase
            item { ShaderShowcase(selectedQuality = selectedQuality) }

            // Component examples
            item { ComponentExamples(selectedQuality = selectedQuality) }

            // Performance info
            item { PerformanceSection() }
        }

        // Floating action
        FloatingActions(
            onSettingsClick = { showSettings = true },
            selectedQuality = selectedQuality,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }

    // Settings bottom sheet
    if (showSettings) {
        SettingsSheet(
            selectedQuality = selectedQuality,
            onQualityChange = { selectedQuality = it },
            onDismiss = { showSettings = false }
        )
    }
}

@Composable
private fun BackgroundPattern() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Geometric pattern
        repeat(8) { index ->
            val offset = index * 80f
            Box(
                modifier =
                Modifier
                    .size((40 + index * 4).dp)
                    .offset(x = (offset % 350).dp, y = (offset * 1.2f % 700).dp)
                    .background(
                        Color.White.copy(alpha = 0.05f),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Liquid Glass",
            style =
            MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = "GPU-Accelerated Glass Effects",
            style =
            MaterialTheme.typography.titleMedium.copy(
                color = Color.Cyan.copy(alpha = 0.9f)
            )
        )
    }
}

@Composable
private fun QualitySelector(
    selectedQuality: LiquidGlassQuality,
    onQualitySelected: (LiquidGlassQuality) -> Unit
) {
    Column {
        Text(
            text = "Quality Mode",
            style =
            MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QualityCard(
                title = "⚡ Performance",
                subtitle = "~0.1ms",
                description = "Box blur",
                quality = LiquidGlassQuality.PERFORMANCE,
                isSelected = selectedQuality == LiquidGlassQuality.PERFORMANCE,
                onClick = { onQualitySelected(LiquidGlassQuality.PERFORMANCE) },
                modifier = Modifier.weight(1f)
            )

            QualityCard(
                title = "⚖️ Balanced",
                subtitle = "~0.3ms",
                description = "Gaussian",
                quality = LiquidGlassQuality.BALANCED,
                isSelected = selectedQuality == LiquidGlassQuality.BALANCED,
                onClick = { onQualitySelected(LiquidGlassQuality.BALANCED) },
                modifier = Modifier.weight(1f)
            )

            QualityCard(
                title = "✨ Premium",
                subtitle = "~0.5ms",
                description = "Full effects",
                quality = LiquidGlassQuality.PREMIUM,
                isSelected = selectedQuality == LiquidGlassQuality.PREMIUM,
                onClick = { onQualitySelected(LiquidGlassQuality.PREMIUM) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QualityCard(
    title: String,
    subtitle: String,
    description: String,
    quality: LiquidGlassQuality,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier.height(100.dp),
        settings =
        LiquidGlassPresets.card.copy(
            quality = quality,
            bezelColor =
            if (isSelected) Color.Cyan.copy(alpha = 0.4f)
            else Color.White.copy(alpha = 0.2f)
        ),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style =
                MaterialTheme.typography.titleSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = subtitle,
                style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Cyan.copy(alpha = 0.8f)
                )
            )
            Text(
                text = description,
                style =
                MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Composable
private fun ShaderShowcase(selectedQuality: LiquidGlassQuality) {
    Column {
        Text(
            text = "Shader Demo",
            style =
            MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        TestRealTimeLiquidGlass(
            quality = selectedQuality,
            blurRadius = 15f,
            refractionStrength = 0.8f,
            chromaticAberration = 1.2f,
            glassThickness = 0.3f,
            glassColor = Color.Cyan.copy(alpha = 0.1f),
            modifier =
            Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp)),
            backgroundContent = {
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            brush =
                            Brush.linearGradient(
                                colors =
                                listOf(
                                    Color(
                                        0xFF6366F1
                                    ),
                                    Color(
                                        0xFF8B5CF6
                                    ),
                                    Color(
                                        0xFF06B6D4
                                    )
                                )
                            )
                        )
                ) {
                    // Animated elements
                    repeat(5) { index ->
                        val offset by
                        animateFloatAsState(
                            targetValue = if (index % 2 == 0) 1f else -1f,
                            animationSpec =
                            infiniteRepeatable(
                                animation = tween(1500 + index * 200),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "glass_animation"
                        )

                        Box(
                            modifier =
                            Modifier
                                .size((30 + index * 10).dp)
                                .offset(
                                    x = (40 + index * 50).dp,
                                    y = (30 + offset * 20).dp
                                )
                                .background(
                                    Color.White.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        )
                    }

                    Text(
                        text = "GPU SHADERS",
                        style =
                        MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            glassContent = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Real-Time Glass",
                            style =
                            MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "80x faster than CPU",
                            style =
                            MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Cyan.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun ComponentExamples(selectedQuality: LiquidGlassQuality) {
    Column {
        Text(
            text = "Components",
            style =
            MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Button example
            LiquidGlassButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                settings = LiquidGlassPresets.button.copy(quality = selectedQuality)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Play", color = Color.White)
                }
            }

            // Card example
            LiquidGlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
                settings = LiquidGlassPresets.card.copy(quality = selectedQuality)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Green.copy(alpha = 0.8f),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Featured",
                        style =
                        MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PerformanceSection() {
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        settings =
        LiquidGlassPresets.card.copy(
            baseColor = Color.Green.copy(alpha = 0.08f),
            bezelColor = Color.Green.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Green.copy(alpha = 0.8f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Performance Optimized",
                    style =
                    MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =
                "• GPU acceleration with OpenGL ES shaders\n" +
                        "• Real-time 60+ FPS rendering\n" +
                        "• Memory optimized texture management\n" +
                        "• Automatic quality scaling",
                style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3f
            )
        }
    }
}

@Composable
private fun FloatingActions(
    onSettingsClick: () -> Unit,
    selectedQuality: LiquidGlassQuality,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Settings FAB
        LiquidGlassFab(
            onClick = onSettingsClick,
            settings =
            LiquidGlassPresets.button.copy(
                quality = selectedQuality,
                specularIntensity = 0.4f
            )
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSheet(
    selectedQuality: LiquidGlassQuality,
    onQualityChange: (LiquidGlassQuality) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color.Transparent) {
        LiquidGlassSheet(
            modifier = Modifier.fillMaxWidth(),
            settings =
            LiquidGlassPresets.card.copy(
                quality = LiquidGlassQuality.PREMIUM,
                blurRadius = 30.dp
            )
        ) {
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                Text(
                    text = "Liquid Glass Settings",
                    style =
                    MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Current Quality: ${selectedQuality.name}",
                    style =
                    MaterialTheme.typography.titleMedium.copy(
                        color = Color.Cyan.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val qualityDescription =
                    when (selectedQuality) {
                        LiquidGlassQuality.PERFORMANCE ->
                            "Optimized for maximum performance with basic blur effects. Perfect for animations and battery-conscious applications."

                        LiquidGlassQuality.BALANCED ->
                            "Balanced approach with good visual quality and performance. Uses Gaussian blur for smooth glass effects."

                        LiquidGlassQuality.PREMIUM ->
                            "Maximum visual fidelity with advanced glass physics, refraction, and chromatic aberration effects."
                    }

                Text(
                    text = qualityDescription,
                    style =
                    MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                LiquidGlassButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    settings = LiquidGlassPresets.button.copy(quality = selectedQuality)
                ) { Text("Close", color = Color.White) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LiquidGlassDemoPreview() {
    LiquidGlassDemo()
}
