package com.example.classter

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class NotasHijoActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_hijo)

        db = FirebaseFirestore.getInstance()

        val contenedorNotas = findViewById<LinearLayout>(R.id.contenedor_notas)
        val nombreHijo = findViewById<TextView>(R.id.txt_nombre_hijo)

        // Recibir ID y nombre del hijo desde el intent
        val hijoId = intent.getStringExtra("hijoId")
        val hijoNombre = intent.getStringExtra("hijoNombre")

        nombreHijo.text = "Notas de: $hijoNombre"

        if (hijoId != null) {
            db.collection("notas")
                .whereEqualTo("estudianteId", hijoId)
                .get()
                .addOnSuccessListener { query ->
                    if (!query.isEmpty) {
                        for (doc in query) {
                            val curso = doc.getString("cursoNombre") ?: "Curso"
                            val nota = doc.get("nota")?.toString() ?: "—"
                            val bimestre = doc.getString("bimestre") ?: "—"

                            // 🔹 Crear tarjeta (contenedor)
                            val card = LinearLayout(this)
                            card.orientation = LinearLayout.VERTICAL
                            card.setPadding(32, 32, 32, 32)

                            // 🔹 Fondo con bordes redondeados
                            val background = GradientDrawable()
                            background.cornerRadius = 40f
                            background.setStroke(3, getColor(android.R.color.darker_gray))

                            // 🔹 Cambiar color según la nota
                            val notaInt = nota.toIntOrNull() ?: 0
                            when {
                                notaInt >= 17 -> background.setColor(0xFFA5D6A7.toInt()) // Verde claro
                                notaInt >= 13 -> background.setColor(0xFFFFF59D.toInt()) // Amarillo claro
                                else -> background.setColor(0xFFEF9A9A.toInt()) // Rojo claro
                            }
                            card.background = background

                            // 🔹 Texto del curso
                            val txtCurso = TextView(this)
                            txtCurso.text = "📘 $curso"
                            txtCurso.textSize = 18f
                            txtCurso.setTextColor(getColor(android.R.color.black))
                            txtCurso.setPadding(0, 0, 0, 8)

                            // 🔹 Texto de la nota
                            val txtNota = TextView(this)
                            txtNota.text = "Nota: $nota"
                            txtNota.textSize = 16f
                            txtNota.setTextColor(getColor(android.R.color.black))

                            // 🔹 Texto del bimestre
                            val txtBimestre = TextView(this)
                            txtBimestre.text = "Bimestre: $bimestre"
                            txtBimestre.textSize = 14f
                            txtBimestre.setTextColor(getColor(android.R.color.darker_gray))
                            txtBimestre.setPadding(0, 4, 0, 0)

                            // 🔹 Agregar textos al card
                            card.addView(txtCurso)
                            card.addView(txtNota)
                            card.addView(txtBimestre)

                            // 🔹 Margen entre tarjetas
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.setMargins(0, 0, 0, 24)
                            card.layoutParams = params

                            // 🔹 Agregar tarjeta al contenedor principal
                            contenedorNotas.addView(card)
                        }
                    } else {
                        val noData = TextView(this)
                        noData.text = "No hay notas registradas."
                        noData.textSize = 16f
                        noData.setPadding(16, 16, 16, 16)
                        contenedorNotas.addView(noData)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar notas", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "No se recibió ID del estudiante", Toast.LENGTH_SHORT).show()
        }
    }
}
