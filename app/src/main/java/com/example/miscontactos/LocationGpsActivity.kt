package com.example.miscontactos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationGpsActivity : AppCompatActivity() {

    // Cliente de Google Play Services que entrega la ubicación del dispositivo
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // TextView donde se mostrará la latitud y longitud-
    lateinit var txtUbicacion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Carga la pantalla definida en res/layout/activity_location_gps.xml
        setContentView(R.layout.activity_location_gps)

        // Evita que el contenido quede debajo de la barra de estado/navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa el cliente de ubicación
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtiene los elementos del XML mediante su id
        txtUbicacion = findViewById(R.id.txt_ubicacion)
        val btnGpsCalcular = findViewById<Button>(R.id.btn_gpscalcular)
        val btnRegresar = findViewById<Button>(R.id.btn_regresar)

        // Al presionar "Obtener ubicación" se llama a findLocation()
        btnGpsCalcular.setOnClickListener {
            findLocation()
        }

        // Al presionar "Volver" se usa un Intent para regresar a MainActivity
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // cierra esta pantalla para no acumularla
        }
    }

    // Obtiene la última ubicación conocida y la muestra en pantalla
    private fun findLocation() {
        // 1. Comprueba si el usuario ya concedió el permiso de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 2. Si no hay permiso, se solicita al usuario
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
            Toast.makeText(this, R.string.gps_pedir_permiso, Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Pide la última ubicación disponible
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // 4. Obtiene latitud y longitud
                val latitud = location.latitude
                val longitud = location.longitude

                // 5. Muestra los valores en el TextView
                txtUbicacion.text = getString(
                    R.string.gps_latitud_longitud,
                    latitud.toString(),
                    longitud.toString()
                )
                Toast.makeText(this, R.string.gps_obtenida, Toast.LENGTH_SHORT).show()
            } else {
                txtUbicacion.text = getString(R.string.gps_no_disponible)
            }
        }
    }
}
