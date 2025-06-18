package com.lightscout.lightlabs.compose.ui

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lightscout.lightlabs.R

@Preview
@Composable
fun FloatingShelves() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF070809),
                    Color(0xFFA8CA49),
                )
            )), contentAlignment = Alignment.Center
    ) {
        var starPosition by remember {
            mutableStateOf(true)
        }

        val transition = updateTransition(targetState = starPosition, label = "shelfTransition")

        val alphaFront = transition.animateFloat(
            transitionSpec = { tween(400, easing = EaseIn) },
            label = "alphaFront"
        ) { if (it) 1f else 0f }

        val xDpFront = transition.animateInt(
            transitionSpec = { tween(300, easing = EaseIn) },
            label = "xDpFront"
        ) { if (it) 0 else 20 }

        val alphaFront2 = transition.animateFloat(
            transitionSpec = { tween(400, easing = EaseIn) },
            label = "alphaFront"
        ) { if (it) 0f else 1f }


        Card(colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
            ConstraintLayout(
                modifier = Modifier
                    .size(300.dp, 300.dp)
                    .clickable {
                        starPosition = !starPosition
                    }
            ) {
                val (back, front) = createRefs()
                if (!starPosition) {
                    Shelf(
                        modifier = Modifier
                            .constrainAs(front) {
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)

                            }
                            .alpha(alphaFront2.value),
                        DpSize(200.dp, 250.dp)
                    )
                    ShelfBack(
                        modifier = Modifier
                            .constrainAs(back) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .size(200.dp, 250.dp))


                } else {

                    // ShelfBack always at the back
                    ShelfBack(
                        modifier = Modifier
                            .constrainAs(back) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .size(200.dp, 250.dp)
                    )

                    // Shelf always at the front but gets temporarily hidden
                    Shelf(
                        modifier = Modifier
                            .constrainAs(front) {
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .alpha(alphaFront.value)
                            .offset(xDpFront.value.dp, 0.dp)
                            ,
                        size = DpSize(200.dp, 250.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ShelfBack(modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        val brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF2F2F2),
                Color(0xFFE4E4E4),
                Color(0xFFDFDFDF),
                Color(0xFFF9F9F9),
                Color(0xFFD5D5D5),
            )
        )

        Box(modifier = Modifier.background(brush)) {

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp), shape = RoundedCornerShape(10),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.image_01),
                    contentDescription = "A young woman",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }


        }
    }
}

@Composable
fun Shelf(modifier: Modifier, size: DpSize = DpSize(125.dp, 125.dp)) {

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10))
            .size(size)
            .padding(4.dp), colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3A590)
        ), shape = RoundedCornerShape(10)
    ) {

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.image_02),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

    }

}
