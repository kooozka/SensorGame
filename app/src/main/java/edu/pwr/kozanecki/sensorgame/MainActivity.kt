package edu.pwr.kozanecki.sensorgame

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.pwr.kozanecki.sensorgame.ui.theme.SensorGameTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private val color = mutableStateOf(Color.Red)
    private val position = mutableStateOf(Offset(150f, 150f))
    private val obstacles = listOf(
        Obstacle(Offset(0f, 0f), size = Size(5f, 1100f)),
        Obstacle(Offset(250f, 0f), size = Size(5f, 1100f)),
        Obstacle(Offset(255f, 1100f), size = Size(-40f, 5f)),
        Obstacle(Offset(0f, 1100f), size = Size(20f, 5f)),
        Obstacle(Offset(20f, 1100f), size = Size(5f, 900f)),
        Obstacle(Offset(215f, 1100f), size = Size(5f, 700f)),
        Obstacle(Offset(20f, 2000f), size = Size(500f, 5f)),
        Obstacle(Offset(215f, 1800f), size = Size(300f, 5f))
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
                        color.value,
                        position.value
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

            color.value = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.Green else Color.Red

            val positionX = position.value.x - sides * 2f
            val positionY = position.value.y + upDown * 2f

            position.value = Offset(positionX, positionY)
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
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
//                    val radius = 20.dp.toPx()
//                    drawCircle(
//                        color = Color.White,
//                        radius = radius,
//                        center = position
//                    )
                    drawRect(Color.White, position, Size(60f, 60f))
                    drawRect(Color.Black, Offset(0f, 0f), size = Size(5f, 1100f))
                    drawRect(Color.Black, Offset(250f, 0f), size = Size(5f, 1100f))
                    drawRect(Color.Black, Offset(255f, 1100f), size = Size(-40f, 5f))
                    drawRect(Color.Black, Offset(0f, 1100f), size = Size(20f, 5f))
                    drawRect(Color.Black, Offset(20f, 1100f), size = Size(5f, 900f))
                    drawRect(Color.Black, Offset(215f, 1100f), size = Size(5f, 700f))
                    drawRect(Color.Black, Offset(20f, 2000f), size = Size(500f, 5f))
                    drawRect(Color.Black, Offset(215f, 1800f), size = Size(300f, 5f))
                }
            }
        if (checkCollides(position, 60.0)) {
            Box(modifier = Modifier
                .size(20.dp)
                .background(Color.Yellow))
        }
    }

    data class Obstacle(val offset: Offset, val size: Size)

    private fun checkCollides(position: Offset, radius: Double): Boolean {
        for (obstacle in obstacles) {
            val obstacleLeft = obstacle.offset.x
            val obstacleRight = obstacle.offset.x + obstacle.size.width
            val obstacleTop = obstacle.offset.y
            val obstacleBottom = obstacle.offset.y + obstacle.size.height

            val ballLeft = (position.x - radius).toFloat()
            val ballRight = (position.x + radius).toFloat()
            val ballTop = (position.y - radius).toFloat()
            val ballBottom = (position.y + radius).toFloat()


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
}
