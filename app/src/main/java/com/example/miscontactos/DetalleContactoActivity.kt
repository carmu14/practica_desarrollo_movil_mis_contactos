package com.example.miscontactos

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalleContactoActivity : AppCompatActivity() {
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

        // Obtiene el botón Volver del XML mediante su id
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // finish() cierra esta pantalla y regresa a MainActivity
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
