package com.example.classter

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class EstudianteHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudiante_home)

        val btnNotas = findViewById<LinearLayout>(R.id.btn_notas)
        val btnHorario = findViewById<LinearLayout>(R.id.btn_horario)
        val btnPerfil = findViewById<LinearLayout>(R.id.btn_perfil)

        // Acciones de ejemplo
        btnNotas.setOnClickListener {
            startActivity(Intent(this, NotasActivity::class.java))
        }

        btnHorario.setOnClickListener {
            startActivity(Intent(this, HorarioActivity::class.java))
        }

        btnPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
    }
}
