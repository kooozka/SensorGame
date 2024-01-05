package edu.pwr.kozanecki.sensorgame

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.pwr.kozanecki.sensorgame.ui.theme.SensorGameTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private val rotationX = mutableStateOf(0f)
    private val rotationY = mutableStateOf(0f)
    private val color = mutableStateOf(Color.Red)


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
                    SimpleSquareWithText(rotationX.value, rotationY.value, color.value)
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
                SensorManager.SENSOR_DELAY_GAME)
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]

            rotationX.value = -upDown * 3f
            rotationY.value = -sides * 3f

            color.value = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.Green else Color.Red
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}

@Composable
fun SimpleSquareWithText(rotX: Float = 0f, rotY: Float = 0f, color: Color) {
    Box(modifier = Modifier
        .padding(vertical = 250.dp, horizontal = 90.dp)
        .graphicsLayer { rotationX = rotX }
        .graphicsLayer { rotationY = rotY },
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = color),
            contentAlignment = Alignment.Center)
        {
            Text(
                modifier = Modifier
                    .graphicsLayer { rotationX = rotX }
                    .graphicsLayer { rotationY = rotY },
                text = "Hello, Jetpack Compose!",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}