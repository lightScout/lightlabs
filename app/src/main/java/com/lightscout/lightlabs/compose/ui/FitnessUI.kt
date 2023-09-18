package com.lightscout.lightlabs.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lightscout.lightlabs.R
import com.lightscout.lightlabs.ui.theme.LightLabsTheme

@Composable
fun FitnessUI() {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF34323A))
    ) {
        val (topSection, middleSection, bottomSection) = createRefs()
        TopSection(modifier = Modifier
            .padding(16.dp)
            .constrainAs(topSection) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

    }

}

@Composable
fun TopSection(modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier.size(100.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Blue)
        ) {

        }

        Spacer(modifier = Modifier.width(16.dp))
        Column() {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Mike Johnson", style = MaterialTheme.typography.displaySmall)
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = "Profile"
                )

            }

        }
    }
}


@Composable
fun GreetingPreview() {
    LightLabsTheme {
        FitnessUI()
    }
}

