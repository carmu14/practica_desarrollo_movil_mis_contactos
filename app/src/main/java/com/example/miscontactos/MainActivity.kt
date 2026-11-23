package com.example.miscontactos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Carga la pantalla definida en res/layout/activity_main.xml
        setContentView(R.layout.activity_main)

        // Evita que el contenido quede debajo de la barra de estado/navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtiene el botón del XML mediante su id
        val btnVerContacto = findViewById<Button>(R.id.btnVerContacto)

        // Acción que se ejecuta al presionar el botón
        btnVerContacto.setOnClickListener {
            // El Intent indica desde qué pantalla (this) y hacia cuál vamos
            val intent = Intent(this, DetalleContactoActivity::class.java)

            // Abre DetalleContactoActivity
            startActivity(intent)
        }

        // Botón que abre la pantalla de ubicación GPS
        val btnVerUbicacion = findViewById<Button>(R.id.btnVerUbicacion)
        btnVerUbicacion.setOnClickListener {
            val intent = Intent(this, LocationGpsActivity::class.java)
            startActivity(intent)
        }
    }
}
