package com.lightscout.lightlabs.compose.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lightscout.lightlabs.R

@Preview
@Composable
fun Flow() {

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (welcome, discover, menu, lineArc, arrowLine, arrowHead, bottomSection, products, nextPage) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.lines_backgrounds1),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .constrainAs(menu) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = "flow.®",
                color = Color(0xFFFFFFFF),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.SansSerif
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_menu_24),
                    contentDescription = "Menu",
                    Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier,
                    text = "MENU",
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.SansSerif
                )
            }

        }

        Text(
            modifier = Modifier
                .constrainAs(welcome) {
                    top.linkTo(menu.bottom)
                    start.linkTo(menu.start)
                }
                .padding(top = 32.dp, start = 16.dp),
            text = "Welcome to\ndesign studio",
            color = Color(0xFFFFFFFF),
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = FontFamily.SansSerif
        )

        Box(modifier = Modifier
            .constrainAs(
                arrowLine
            ) {
                bottom.linkTo(discover.top)
                start.linkTo(discover.end)
            }
            .offset(x = (-15).dp)) {
            Canvas(modifier = Modifier
                .size(width = 1.dp, height = 300.dp),
                onDraw = {
                    drawLine(
                        start = Offset(0f, 0f),
                        end = Offset(0f, this.size.height),
                        color = Color(0xFFFFFFFF),
                        strokeWidth = 5f
                    )
                })
        }

        Image(modifier = Modifier
            .constrainAs(
                arrowHead
            ) {
                bottom.linkTo(arrowLine.top)
                start.linkTo(arrowLine.start)
            }
            .offset(x = (-30).dp, y = 18.dp)
            .size(30.dp)
            .background(Color.Transparent),
            painter = painterResource(id = R.drawable.arrow_tip_up), contentDescription = "")



        Box(Modifier
            .background(Color.Transparent)
            .constrainAs(lineArc) {
                top.linkTo(welcome.bottom)
                start.linkTo(welcome.start)
            }
            .padding(top = 8.dp, start = 20.dp)) {

            Canvas(Modifier.size(width = 225.dp, height = 450.dp),
                onDraw = {
                    drawLine(
                        start = Offset(0f, 0f),
                        end = Offset(0f, this.size.height),
                        color = Color(0xFFFFFFFF),
                        strokeWidth = 5f
                    )
                    drawLine(
                        start = Offset(this.size.width, this.size.height),
                        end = Offset(this.size.width, this.size.height / 1.8.toFloat()),
                        color = Color(0xFFFFFFFF),
                        strokeWidth = 5f
                    )
                    drawArc(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFFFFF),
                                Color(0xFFFFFFFF),
                            )
                        ),
                        size = Size(this.size.width, this.size.height / 2),
                        topLeft = Offset(0f, this.size.height / 1.35.toFloat()),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(
                            width = 5f
                        ),
                    )

                    drawCircle(
                        color = Color(0x5E030303),
                        radius = this.size.width / 2.1.toFloat(),
                        center = Offset(this.size.width / 2, this.size.height / 1.01.toFloat()),
                    )

                })
        }

        Column(
            modifier = Modifier
                .constrainAs(products) {
                    start.linkTo(lineArc.start)
                    end.linkTo(lineArc.end)
                    top.linkTo(lineArc.bottom)
                }
                .size(225.dp, 100.dp)
                .padding(start = 20.dp)
                .offset(y = (-50).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_link_24),
                contentDescription = ""
            )

            Text(
                text = "OUR PRODUCTS",
                color = Color(0xFFFFFFFF),
                style = MaterialTheme.typography.titleSmall,
                fontFamily = FontFamily.SansSerif
            )


        }

        Box(modifier = Modifier
            .constrainAs(
                discover
            ) {
                bottom.linkTo(lineArc.bottom)
                top.linkTo(lineArc.top)
                start.linkTo(lineArc.end)
            }
            .offset(x = (-5).dp)) {

            Text(
                text = "Discover",
                color = Color(0xFFFFFFFF),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.SansSerif
            )
        }

        Card(
            modifier = Modifier
                .constrainAs(
                    nextPage
                ) {
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomSection.top)

                }
                .padding(horizontal = 16.dp, vertical = 48.dp), colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = CircleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color(0xFF000000)),
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )

        }


        Row(
            modifier = Modifier
                .constrainAs(bottomSection) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "flow © 2023",
                color = Color(0xFFFFFFFF),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.SansSerif
            )
            Row {
                Text(
                    text = "We bring the raw to the ",
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "beautiful",
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
            }


        }


    }

}