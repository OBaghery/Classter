package com.example.classter

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HijosActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hijos)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val contenedorHijos = findViewById<LinearLayout>(R.id.contenedor_hijos)
        val user = auth.currentUser

        if (user != null) {
            // 🔹 Obtener documento del apoderado
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val hijos = document.get("hijos") as? List<String>
                        if (!hijos.isNullOrEmpty()) {
                            for (hijoId in hijos) {
                                // 🔹 Buscar cada hijo por su UID en la colección "users"
                                db.collection("users").document(hijoId).get()
                                    .addOnSuccessListener { hijoDoc ->
                                        if (hijoDoc.exists()) {
                                            val nombre = hijoDoc.getString("nombre") ?: "Sin nombre"
                                            val grado = hijoDoc.getString("grado") ?: "—"
                                            val seccion = hijoDoc.getString("seccion") ?: "—"

                                            // 🔹 Crear TextView dinámico para cada hijo
                                            val textView = TextView(this)
                                            textView.text = "👦 $nombre\nGrado: $grado - Sección: $seccion"
                                            textView.textSize = 16f
                                            textView.setTextColor(getColor(android.R.color.black))
                                            textView.setPadding(16, 16, 16, 16)

                                            // 🔹 Al tocar el nombre del hijo, abre las notas
                                            textView.setOnClickListener {
                                                val intent = Intent(this, NotasHijoActivity::class.java)
                                                intent.putExtra("hijoId", hijoId)
                                                intent.putExtra("hijoNombre", nombre)
                                                startActivity(intent)
                                            }

                                            // Añadir el TextView al contenedor
                                            contenedorHijos.addView(textView)
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Error al cargar hijo: $hijoId", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "No hay hijos registrados", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "No se encontró el documento del apoderado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al obtener datos del apoderado", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
