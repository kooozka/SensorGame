package edu.pwr.kozanecki.sensorgame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val obstacles = listOf(
        Obstacle(Offset(0f, 0f), size = Size(245f, 5f)),
        Obstacle(Offset(0f, 0f), size = Size(5f, 1100f)),
        Obstacle(Offset(100f, 250f), size = Size(5f, 700f)),
        Obstacle(Offset(240f, 0f), size = Size(5f, 1100f)),
        Obstacle(Offset(145f, 1100f), size = Size(100f, 5f)),
        Obstacle(Offset(0f, 1100f), size = Size(20f, 5f)),
        Obstacle(Offset(20f, 1100f), size = Size(5f, 850f)),
        Obstacle(Offset(190f, 1100f), size = Size(5f, 700f)),
        Obstacle(Offset(20f, 1300f), size = Size(70f, 5f)),
        Obstacle(Offset(20f, 1950f), size = Size(720f, 5f)),
        Obstacle(Offset(150f, 1800f), size = Size(300f, 5f)),
        Obstacle(Offset(450f, 1900f), size = Size(5f, 50f)),
        Obstacle(Offset(445f, 1710f), size = Size(5f, 90f)),
        Obstacle(Offset(195f, 1710f), size = Size(250f, 5f)),
        Obstacle(Offset(600f, 1530f), size = Size(5f, 320f)),
        Obstacle(Offset(300f, 1600f), size = Size(300f, 5f)),
        Obstacle(Offset(300f, 1250f), size = Size(5f, 350f)),
        Obstacle(Offset(190f, 1250f), size = Size(110f, 5f)),
        Obstacle(Offset(740f, 1630f), size = Size(5f, 325f)),
        Obstacle(Offset(740f, 1650f), size = Size(300f, 5f)),
        Obstacle(Offset(1035f, 1250f), size = Size(5f, 400f)),
        Obstacle(Offset(600f, 1530f), size = Size(300f, 5f)),
        Obstacle(Offset(895f, 1380f), size = Size(5f, 150f)),
        Obstacle(Offset(740f, 1250f), size = Size(300f, 5f)),
        Obstacle(Offset(550f, 1375f), size = Size(350f, 5f)),
        Obstacle(Offset(550f, 980f), size = Size(5f, 400f)),
        Obstacle(Offset(740f, 980f), size = Size(5f, 270f)),
        Obstacle(Offset(695f, 980f), size = Size(45f, 5f)),
        Obstacle(Offset(550f, 980f), size = Size(45f, 5f)),
        Obstacle(Offset(590f, 880f), size = Size(5f, 100f)),
        Obstacle(Offset(695f, 880f), size = Size(5f, 100f)),
//        Obstacle(Offset(500f, 885f), size = Size(90f, 5f)),
//        Obstacle(Offset(695f, 885f), size = Size(90f, 5f)),
//        Obstacle(Offset(784f, 600f), size = Size(5f, 290f)),
//        Obstacle(Offset(498f, 600f), size = Size(5f, 290f)),
//        Obstacle(Offset(498f, 600f), size = Size(290f, 5f))
    )

    data class Obstacle(val offset: Offset, val size: Size)



    @Composable
    fun DrawPath() {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ){
            drawRect(Color.Black, Offset(0f, 0f), size = Size(245f, 5f))
            drawRect(Color.Black, Offset(0f, 0f), size = Size(5f, 1100f))
            drawRect(Color.Black, Offset(100f, 250f), size = Size(5f, 700f))
            drawRect(Color.Black, Offset(240f, 0f), size = Size(5f, 1100f))
            drawRect(Color.Black, Offset(245f, 1100f), size = Size(-100f, 5f))
            drawRect(Color.Black, Offset(0f, 1100f), size = Size(20f, 5f))
            drawRect(Color.Black, Offset(20f, 1100f), size = Size(5f, 850f))
            drawRect(Color.Black, Offset(190f, 1100f), size = Size(5f, 700f))
            drawRect(Color.Black, Offset(20f, 1300f), size = Size(70f, 5f))
            drawRect(Color.Black, Offset(20f, 1950f), size = Size(720f, 5f))
            drawRect(Color.Black, Offset(150f, 1800f), size = Size(300f, 5f))
            drawRect(Color.Black, Offset(450f, 1950f), size = Size(5f, -50f))
            drawRect(Color.Black, Offset(445f, 1800f), size = Size(5f, -90f))
            drawRect(Color.Black, Offset(445f, 1710f), size = Size(-250f, 5f))
            drawRect(Color.Black, Offset(600f, 1850f), size = Size(5f, -320f))
            drawRect(Color.Black, Offset(600f, 1600f), size = Size(-300f, 5f))
            drawRect(Color.Black, Offset(300f, 1600f), size = Size(5f, -350f))
            drawRect(Color.Black, Offset(300f, 1250f), size = Size(-110f, 5f))
            drawRect(Color.Black, Offset(740f, 1955f), size = Size(5f, -325f))
            drawRect(Color.Black, Offset(740f, 1650f), size = Size(300f, 5f))
            drawRect(Color.Black, Offset(1040f, 1650f), size = Size(-5f, -400f))
            drawRect(Color.Black, Offset(600f, 1530f), size = Size(300f, 5f))
            drawRect(Color.Black, Offset(895f, 1530f), size = Size(5f, -150f))
            drawRect(Color.Black, Offset(1040f, 1250f), size = Size(-300f, 5f))
            drawRect(Color.Black, Offset(900f, 1380f), size = Size(-350f, -5f))
            drawRect(Color.Black, Offset(550f, 1380f), size = Size(5f, -400f))
            drawRect(Color.Black, Offset(740f, 1250f), size = Size(5f, -270f))
            drawRect(Color.Black, Offset(740f, 980f), size = Size(-45f, 5f))
            drawRect(Color.Black, Offset(550f, 980f), size = Size(45f, 5f))
            drawRect(Color.Black, Offset(590f, 980f), size = Size(5f, -100f))
            drawRect(Color.Black, Offset(695f, 980f), size = Size(5f, -100f))
            drawRect(Color.Black, Offset(695f, 890f), size = Size(90f, -5f))
            drawRect(Color.Black, Offset(784f, 890f), size = Size(5f, -290f))
            drawRect(Color.Black, Offset(590f, 890f), size = Size(-90f, -5f))
            drawRect(Color.Black, Offset(498f, 890f), size = Size(5f, -290f))
            drawRect(Color.Black, Offset(498f, 600f), size = Size(290f, 5f))
        }
        Box(modifier = Modifier
            .offset(179.dp, 215.dp)
            .size(100.dp)
            .background(Color.White))
    }