package com.example.geolocationpractice

import android.annotation.SuppressLint
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var txtLocation: TextView

    // Manejador para solicitar permisos de ubicación
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtLocation = findViewById(R.id.txtLocation)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Verificamos permisos y obtenemos la ubicación
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location -> 
                if (location != null) {
                    updateLocationUI(location)
                } else {
                    // Configuramos un request con parámetros un poco más amplios
                    val locationRequest = LocationRequest.create().apply {
                        interval = 1000         // 1 segundo de intervalo
                        fastestInterval = 500   // 0.5 segundos de intervalo mínimo
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        numUpdates = 1          // Solo necesitamos una actualización
                    }
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                val freshLocation = locationResult.lastLocation
                                if (freshLocation != null) {
                                    updateLocationUI(freshLocation)
                                } else {
                                    txtLocation.text = "Ubicación no disponible"
                                }
                                // Detenemos las actualizaciones una vez recibida la ubicación
                                fusedLocationClient.removeLocationUpdates(this)
                            }
                        },
                        mainLooper
                    )
                }
            }
            .addOnFailureListener { exception ->
                txtLocation.text = "Error al obtener ubicación"
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateLocationUI(location: Location) {
        val lat = location.latitude
        val lon = location.longitude
        txtLocation.text = "Latitud: $lat, Longitud: $lon"
    }

}
