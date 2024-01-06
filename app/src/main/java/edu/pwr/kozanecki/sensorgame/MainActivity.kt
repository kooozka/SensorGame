package edu.pwr.kozanecki.sensorgame

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.pwr.kozanecki.sensorgame.ui.theme.SensorGameTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private val startingPosition = Offset(GameParameters.STARTING_POSITION_X, GameParameters.STARTING_POSITION_Y)
    private lateinit var sensorManager: SensorManager
    private val backgroundColor = mutableStateOf(Color.Green)
    private val currentPosition = mutableStateOf(Offset(100f, 100f))
    private val gameRunning = mutableStateOf(true)
    private val obstacles = listOf(
        Obstacle(Offset(0f, 0f), size = Size(245f, 5f)),
        Obstacle(Offset(0f, 0f), size = Size(5f, 1100f)),
        Obstacle(Offset(100f, 250f), size = Size(5f, 700f)),
        Obstacle(Offset(240f, 0f), size = Size(5f, 1100f)),
        Obstacle(Offset(245f, 1100f), size = Size(-100f, 5f)),
        Obstacle(Offset(0f, 1100f), size = Size(20f, 5f)),
        Obstacle(Offset(20f, 1100f), size = Size(5f, 850f)),
        Obstacle(Offset(190f, 1100f), size = Size(5f, 700f)),
        Obstacle(Offset(20f, 1300f), size = Size(70f, 5f)),
        Obstacle(Offset(20f, 1950f), size = Size(720f, 5f)),
        Obstacle(Offset(150f, 1800f), size = Size(300f, 5f)),
        Obstacle(Offset(450f, 1950f), size = Size(5f, -50f)),
        Obstacle(Offset(445f, 1800f), size = Size(5f, -90f)),
        Obstacle(Offset(445f, 1710f), size = Size(-250f, 5f)),
        Obstacle(Offset(600f, 1850f), size = Size(5f, -320f)),
        Obstacle(Offset(600f, 1600f), size = Size(-300f, 5f)),
        Obstacle(Offset(300f, 1600f), size = Size(5f, -350f)),
        Obstacle(Offset(300f, 1250f), size = Size(-110f, 5f)),
        Obstacle(Offset(740f, 1955f), size = Size(5f, -325f)),
        Obstacle(Offset(740f, 1650f), size = Size(300f, 5f)),
        Obstacle(Offset(1040f, 1650f), size = Size(-5f, -400f)),
        Obstacle(Offset(600f, 1530f), size = Size(300f, 5f)),
        Obstacle(Offset(895f, 1530f), size = Size(5f, -150f)),
        Obstacle(Offset(1040f, 1250f), size = Size(-300f, 5f)),
        Obstacle(Offset(900f, 1380f), size = Size(-350f, -5f)),
        Obstacle(Offset(550f, 1380f), size = Size(5f, -400f)),
        Obstacle(Offset(740f, 1250f), size = Size(5f, -270f)),
        Obstacle(Offset(740f, 980f), size = Size(-45f, 5f)),
        Obstacle(Offset(550f, 980f), size = Size(45f, 5f)),
        Obstacle(Offset(590f, 980f), size = Size(5f, -100f)),
        Obstacle(Offset(695f, 980f), size = Size(5f, -100f)),
        Obstacle(Offset(695f, 890f), size = Size(90f, -5f)),
        Obstacle(Offset(784f, 890f), size = Size(5f, -290f)),
        Obstacle(Offset(590f, 890f), size = Size(-90f, -5f)),
        Obstacle(Offset(498f, 890f), size = Size(5f, -290f)),
        Obstacle(Offset(498f, 600f), size = Size(290f, 5f))
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSensorStuff()
        setContent {
            SensorGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleSquareWithText(
                        backgroundColor.value,
                        currentPosition.value
                    )
                }
            }
        }
    }

    private fun setUpSensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1] - 6f

            val positionX = currentPosition.value.x - sides * 2f
            val positionY = currentPosition.value.y + upDown * 2f

            currentPosition.value = Offset(positionX, positionY)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    @Composable
    fun SimpleSquareWithText(color: Color, position: Offset) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = color)
            )
            {
                if (gameRunning.value) {
                    backgroundColor.value = Color.Green
                    DrawSquare(position = position)
                }
                DrawPath()
            }
        if (checkCollides(position)) {
            gameRunning.value = false
        }
        if (checkWin(position)) {
            ShowWinningDialog()
        }
        if (!gameRunning.value){
            backgroundColor.value = Color.Red
            ShowLostDialog()
        }
    }
    
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
    
    @Composable
    fun DrawSquare(position: Offset) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawRect(Color.White, position, Size(GameParameters.SQUARE_SIZE, GameParameters.SQUARE_SIZE))
        }
    }

    @Composable
    fun ShowLostDialog() {
        Box(modifier = Modifier
            .padding(horizontal = 50.dp, vertical = 150.dp)
            .background(Color.Yellow),
            contentAlignment = Alignment.Center   ) {
            Column(verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "YOU LOST",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black)
                Button(onClick = {
                    gameRunning.value = true
                    currentPosition.value = startingPosition
                }) {
                    Text(text = "Play again")
                }
            }
        }
    }

    @Composable
    fun ShowWinningDialog() {
        Box(modifier = Modifier
            .padding(horizontal = 50.dp, vertical = 150.dp)
            .background(Color.Yellow),
            contentAlignment = Alignment.Center   ) {
            Column(verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "YOU WON",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black)
                Button(onClick = {
                    gameRunning.value = true
                    currentPosition.value = startingPosition
                }) {
                    Text(text = "Play again")
                }
            }
        }
    }
    

    data class Obstacle(val offset: Offset, val size: Size)

    private fun checkCollides(position: Offset): Boolean {
        for (obstacle in obstacles) {
            val obstacleLeft = obstacle.offset.x
            val obstacleRight = obstacle.offset.x + obstacle.size.width
            val obstacleTop = obstacle.offset.y
            val obstacleBottom = obstacle.offset.y + obstacle.size.height

            val ballLeft = position.x
            val ballRight = position.x + GameParameters.SQUARE_SIZE
            val ballTop = position.y
            val ballBottom = position.y + GameParameters.SQUARE_SIZE


            if (ballLeft < obstacleRight &&
                ballRight > obstacleLeft &&
                ballTop < obstacleBottom &&
                ballBottom > obstacleTop
            ) {
                return true
            }
        }
        return false
    }

    private fun checkWin(position: Offset): Boolean {
        if (position.x > 498 && position.x + (GameParameters.SQUARE_SIZE) < 784 && (position.y + GameParameters.SQUARE_SIZE) < 890 && position.y > 600){
            return true
        }
        return false
    }
}
