package com.lightscout.lightlabs.compose.animaiton

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lightscout.lightlabs.R

@Composable
fun AnimatedContentSize() {
    var expand by remember {
        mutableStateOf(false)
    }
    val text =
        "Coding isn't just the act of writing commands for a machine. It's the art of crafting thoughts into tangible solutions, painting dreams with logic and syntax. Every line of code is a step towards a brighter future, an ode to human potential. Embrace this journey, for in the world of coding, every challenge surmounted is a testament to the indomitable spirit of innovation. Code on, and let your creativity light the way!"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.Black),
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    expand = !expand
                }) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.light_labs_logo),
                    contentDescription = "Light Labs Logo",
                    Modifier.size(200.dp)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Justify,
                    maxLines = if (!expand) 2 else 20,
                    modifier = Modifier
                        .animateContentSize(animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ))
                        .padding(horizontal = 34.dp, vertical = 16.dp)
                )

            }

        }


    }
}