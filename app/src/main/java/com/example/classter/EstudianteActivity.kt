package com.example.classter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EstudianteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudiante)

        val btnCursos = findViewById<Button>(R.id.btn_cursos)
        val btnNotas = findViewById<Button>(R.id.btn_notas)
        val btnHorario = findViewById<Button>(R.id.btn_horario)
        val btnPerfil = findViewById<Button>(R.id.btn_perfil)

        btnCursos.setOnClickListener {
            startActivity(Intent(this, CursosActivity::class.java))
        }

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
