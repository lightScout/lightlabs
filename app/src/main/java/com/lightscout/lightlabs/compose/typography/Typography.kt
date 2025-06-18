package com.lightscout.lightlabs.compose.typography

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lightscout.lightlabs.R

@Preview
@Composable
fun Typography() {

    val bodyLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
        baselineShift = BaselineShift.Subscript,
        color = Color.Black.copy(alpha = 0.8f),
        textAlign = TextAlign.Justify
    )

    val bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        letterSpacing = 0.15.sp,
        baselineShift = BaselineShift.Subscript,
        color = Color.Black.copy(alpha = 0.5f),
        textAlign = TextAlign.Justify
    )

    val replyTypography = Typography(
        bodyLarge = bodyLarge,
        bodyMedium = bodyMedium
    )

    MaterialTheme(
        typography = replyTypography
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEAE5DA)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFE9)),
                modifier = Modifier
                    .padding(16.dp)
                    
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Card(shape = CircleShape, modifier = Modifier.size(64.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.image_01),
                                    contentDescription = "Profile Picture",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Jane Doe",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Justify,
                                    modifier = Modifier
                                        .animateContentSize(
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                )
                                Text(
                                    text = "22 minutes ago",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black.copy(alpha = 0.5f),
                                    textAlign = TextAlign.Justify,
                                    modifier = Modifier
                                        .animateContentSize(
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                )
                            }

                        }
                        Spacer(modifier = Modifier.width(128.dp))
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = .8f))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_star_outline_24),
                                contentDescription = "More",
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.8f)),
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(8.dp)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column {

                        Text(
                            text = "Fashion Week London",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "I will be meeting you guys at the gallery, running a bit late but I will be there soon.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        )

                    }

                }


            }
        }

    }
}