package com.example.sensorpractice

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.sensorpractice.ui.theme.SensorPracticeTheme

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null // Declarar como null

    // Estado para almacenar los valores del sensor
    private var sensorValues = mutableStateOf("Valores del sensor:\nX: 0.0\nY: 0.0\nZ: 0.0 ")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar el SensorManager y el sensor de acelerómetro
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Verificar si el dispositivo tiene un acelerómetro
        if (accelerometer == null) {
            sensorValues.value = "Este dispositivo no tiene un acelerómetro."
        }

        setContent {
            SensorPracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorDisplay(
                        sensorValues = sensorValues.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Registrar el listener del sensor si el acelerómetro está disponible
        accelerometer?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Desregistrar el listener del sensor para ahorrar batería
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Este método se llama cada vez que el sensor detecta un cambio
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Actualizar el estado con los nuevos valores del sensor
            sensorValues.value = "Valores del sensor:\nX: $x\nY: $y\nZ: $z \n \n" +
                    " Anderson Castro \n" +
                    " U20190338"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Este método se llama cuando la precisión del sensor cambia
    }
}

@Composable
fun SensorDisplay(sensorValues: String, modifier: Modifier = Modifier) {
    Text(
        text = sensorValues,
        modifier = modifier,
        fontSize = 18.sp
    )
}

@Preview(showBackground = true)
@Composable
fun SensorDisplayPreview() {
    SensorPracticeTheme {
        SensorDisplay("Valores del sensor:\nX: 0.0\nY: 0.0\nZ: 0.0 ")
    }
}