package com.lightscout.lightlabs.liquidglass

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** A button with Liquid Glass effect */
@Composable
fun LiquidGlassButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        settings: LiquidGlassSettings =
                LiquidGlassSettings(
                        borderRadius = 12.dp,
                        baseColor = Color.White.copy(alpha = 0.25f),
                        bezelColor = Color.White.copy(alpha = 0.6f)
                ),
        enabled: Boolean = true,
        content: @Composable RowScope.() -> Unit
) {
    LiquidGlass(
            modifier = modifier.height(48.dp).defaultMinSize(minWidth = 88.dp),
            settings = settings,
            onClick = onClick,
            enabled = enabled
    ) {
        Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
        ) { content() }
    }
}

/** A card container with Liquid Glass effect */
@Composable
fun LiquidGlassCard(
        modifier: Modifier = Modifier,
        settings: LiquidGlassSettings =
                LiquidGlassSettings(
                        borderRadius = 16.dp,
                        baseColor = Color.White.copy(alpha = 0.15f),
                        blurRadius = 24.dp
                ),
        onClick: (() -> Unit)? = null,
        useRenderScript: Boolean = true,
        content: @Composable () -> Unit
) {
    LiquidGlass(
            modifier = modifier,
            settings = settings,
            onClick = onClick,
            useRenderScript = useRenderScript
    ) { Box(modifier = Modifier.fillMaxSize().padding(16.dp)) { content() } }
}

/** A navigation bar with Liquid Glass effect */
@Composable
fun LiquidGlassNavigationBar(
        modifier: Modifier = Modifier,
        settings: LiquidGlassSettings =
                LiquidGlassSettings(
                        borderRadius = 20.dp,
                        baseColor = Color.Black.copy(alpha = 0.1f),
                        blurRadius = 30.dp,
                        bezelWidth = 0.5.dp
                ),
        content: @Composable RowScope.() -> Unit
) {
    LiquidGlass(modifier = modifier.fillMaxWidth().height(64.dp), settings = settings) {
        Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
        ) { content() }
    }
}

/** A toolbar with Liquid Glass effect */
@Composable
fun LiquidGlassToolbar(
        title: String,
        modifier: Modifier = Modifier,
        settings: LiquidGlassSettings =
                LiquidGlassSettings(
                        borderRadius = 0.dp,
                        baseColor = Color.White.copy(alpha = 0.1f),
                        blurRadius = 20.dp
                ),
        navigationIcon: @Composable (() -> Unit)? = null,
        actions: @Composable RowScope.() -> Unit = {}
) {
    LiquidGlass(modifier = modifier.fillMaxWidth().height(56.dp), settings = settings) {
        Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            navigationIcon?.invoke()

            Text(
                    text = title,
                    style =
                            MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White.copy(alpha = 0.9f)
                            ),
                    modifier =
                            Modifier.weight(1f)
                                    .padding(
                                            horizontal = if (navigationIcon != null) 16.dp else 0.dp
                                    )
            )

            Row { actions() }
        }
    }
}

/** A floating action button with Liquid Glass effect */
@Composable
fun LiquidGlassFab(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        settings: LiquidGlassSettings =
                LiquidGlassSettings(
                        borderRadius = 28.dp,
                        baseColor = Color.White.copy(alpha = 0.3f),
                        bezelColor = Color.White.copy(alpha = 0.8f),
                        specularIntensity = 1f
                ),
        content: @Composable () -> Unit
) {
    LiquidGlass(modifier = modifier.size(56.dp), settings = settings, onClick = onClick) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { content() }
    }
}

/** A sheet/modal with Liquid Glass effect */
@Composable
fun LiquidGlassSheet(
        modifier: Modifier = Modifier,
        settings: LiquidGlassSettings =
                LiquidGlassSettings(
                        borderRadius = 20.dp,
                        baseColor = Color.White.copy(alpha = 0.2f),
                        blurRadius = 40.dp,
                        bezelWidth = 1.dp
                ),
        content: @Composable () -> Unit
) {
    LiquidGlass(modifier = modifier, settings = settings) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            // Handle indicator
            Box(modifier = Modifier.width(40.dp).height(4.dp).align(Alignment.CenterHorizontally)) {
                LiquidGlass(
                        settings =
                                LiquidGlassSettings(
                                        borderRadius = 2.dp,
                                        baseColor = Color.White.copy(alpha = 0.5f)
                                )
                ) {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}
