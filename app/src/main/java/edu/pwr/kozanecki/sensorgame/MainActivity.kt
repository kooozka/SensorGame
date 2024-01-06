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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import edu.pwr.kozanecki.sensorgame.ui.theme.SensorGameTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private val startingPosition = Offset(GameParameters.STARTING_POSITION_X, GameParameters.STARTING_POSITION_Y)
    private lateinit var sensorManager: SensorManager
    private val backgroundColor = mutableStateOf(Color.Green)
    private val currentPosition = mutableStateOf(Offset(100f, 100f))
    private val gameRunning = mutableStateOf(true)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSensorStuff()
        setContent {
            SensorGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameInterface(
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
    fun GameInterface(color: Color, position: Offset) {
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
            ShowDialog(message = stringResource(id = R.string.you_won))
        }
        if (!gameRunning.value){
            backgroundColor.value = Color.Red
            ShowDialog(message = stringResource(id = R.string.you_lost))
        }
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
    fun ShowDialog(message: String) {
        Box(modifier = Modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.dialog_horizontal_padding),
                vertical = dimensionResource(id = R.dimen.dialog_vertical_padding
                )
            )
            .background(Color.Yellow),
            contentAlignment = Alignment.Center   ) {
            Column(verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = message,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black)
                Button(onClick = {
                    gameRunning.value = true
                    currentPosition.value = startingPosition
                }) {
                    Text(text = stringResource(R.string.play_again))
                }
            }
        }
    }
    }

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

