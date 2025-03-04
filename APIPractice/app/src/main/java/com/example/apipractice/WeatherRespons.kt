package com.example.apipractice

data class WeatherResponse(
    val name: String, // Nombre de la ciudad
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val sys: Sys,
    val visibility: Int,
    val dt: Long // Fecha y hora de la medición (timestamp)
)

data class Main(
    val temp: Double, // Temperatura actual
    val feels_like: Double, // Sensación térmica
    val humidity: Int, // Humedad
    val pressure: Int // Presión atmosférica
)

data class Weather(
    val description: String, // Descripción del clima
    val icon: String // Icono del clima
)

data class Wind(
    val speed: Double // Velocidad del viento
)

data class Sys(
    val sunrise: Long, // Hora del amanecer (timestamp)
    val sunset: Long // Hora del atardecer (timestamp)
)

//https://api.openweathermap.org/data/2.5/weather?q=rusia&appid=Pegar_aqui_su_apiKey&units=metric