package com.example.apipractice

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Configurar el botón
        findViewById<Button>(R.id.btnGetWeather).setOnClickListener {
            fetchWeatherData()
        }
    }

    private fun fetchWeatherData() {
        val apiKey = "your_API_key"
        val city = "City"

        apiService.getWeather(city, apiKey).enqueue(object : retrofit2.Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: retrofit2.Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    weatherData?.let {
                        // Convertir timestamps a horas legibles
                        val sunriseTime = convertTimestampToTime(it.sys.sunrise)
                        val sunsetTime = convertTimestampToTime(it.sys.sunset)
                        val measurementTime = convertTimestampToTime(it.dt)

                        // Crear el texto con todos los datos
                        val weatherText = """
                    Ciudad: ${it.name}
                    Temperatura: ${it.main.temp}°C
                    Sensación térmica: ${it.main.feels_like}°C
                    Humedad: ${it.main.humidity}%
                    Presión: ${it.main.pressure} hPa
                    Velocidad del viento: ${it.wind.speed} m/s
                    Visibilidad: ${it.visibility / 1000} km
                    Clima: ${it.weather[0].description}
                    Amanecer: $sunriseTime
                    Atardecer: $sunsetTime
                    Última medición: $measurementTime
                """.trimIndent()

                        // Mostrar el texto en el TextView
                        findViewById<TextView>(R.id.tvWeatherData).text = weatherText
                    }
                } else {
                    findViewById<TextView>(R.id.tvWeatherData).text = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                findViewById<TextView>(R.id.tvWeatherData).text = "Error: ${t.message}"
            }
            private fun convertTimestampToTime(timestamp: Long): String {
                val date = Date(timestamp * 1000) // Convertir a milisegundos
                val format = SimpleDateFormat("HH:mm", Locale.getDefault())
                return format.format(date)
            }
        })
    }
}