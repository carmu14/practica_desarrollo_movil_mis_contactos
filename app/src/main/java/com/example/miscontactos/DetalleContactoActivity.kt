package com.example.miscontactos

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalleContactoActivity : AppCompatActivity() {

    private lateinit var txtLatitud: TextView
    private lateinit var txtLongitud: TextView

    // Muestra al usuario el cuadro de diálogo para pedir el permiso de ubicación
    // y recibe su respuesta (permitir o rechazar)
    private val pedirPermiso =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultado ->
            if (resultado.values.any { it }) {
                obtenerUbicacion()
            } else {
                txtLatitud.text = getString(R.string.ubicacion_sin_permiso)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Carga la pantalla definida en res/layout/activity_detalle_contacto.xml
        setContentView(R.layout.activity_detalle_contacto)

        // Evita que el contenido quede debajo de la barra de estado/navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtiene los textos donde se mostrará la ubicación
        txtLatitud = findViewById(R.id.txtLatitud)
        txtLongitud = findViewById(R.id.txtLongitud)

        // Obtiene el botón Volver del XML mediante su id
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // finish() cierra esta pantalla y regresa a MainActivity
        btnVolver.setOnClickListener {
            finish()
        }

        // Si ya tenemos permiso, leemos la ubicación; si no, lo pedimos
        if (tienePermiso(Manifest.permission.ACCESS_FINE_LOCATION) ||
            tienePermiso(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            obtenerUbicacion()
        } else {
            pedirPermiso.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Revisa si el usuario ya concedió un permiso
    private fun tienePermiso(permiso: String): Boolean =
        ContextCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED

    // Pide al sistema la ubicación actual una sola vez.
    // @SuppressLint: los permisos se verifican en las primeras líneas de esta función.
    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {
        val precisa = tienePermiso(Manifest.permission.ACCESS_FINE_LOCATION)
        val aproximada = tienePermiso(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (!precisa && !aproximada) return

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Elige la fuente: GPS (requiere permiso preciso) o red (Wi-Fi / datos móviles)
        val proveedor = when {
            precisa && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER
            else -> null
        }

        if (proveedor == null) {
            txtLatitud.text = getString(R.string.ubicacion_desactivada)
            return
        }

        txtLatitud.text = getString(R.string.ubicacion_cargando)

        // Cuando llega la ubicación, se ejecuta mostrarUbicacion en el hilo principal
        LocationManagerCompat.getCurrentLocation(
            locationManager,
            proveedor,
            null as CancellationSignal?,
            ContextCompat.getMainExecutor(this)
        ) { ubicacion: Location? -> mostrarUbicacion(ubicacion) }
    }

    // Escribe la latitud y longitud en pantalla
    private fun mostrarUbicacion(ubicacion: Location?) {
        if (ubicacion == null) {
            txtLatitud.text = getString(R.string.ubicacion_no_disponible)
            return
        }
        txtLatitud.text = getString(R.string.latitud, ubicacion.latitude.toString())
        txtLongitud.text = getString(R.string.longitud, ubicacion.longitude.toString())
    }
}
