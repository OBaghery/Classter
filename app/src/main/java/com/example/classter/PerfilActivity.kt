package com.example.classter

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Referencias a Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Referencias a los TextView del XML
        val txtNombre = findViewById<TextView>(R.id.txt_nombre)
        val txtCorreo = findViewById<TextView>(R.id.txt_correo)
        val txtGrado = findViewById<TextView>(R.id.txt_grado)
        val txtSeccion = findViewById<TextView>(R.id.txt_seccion)
        val txtRol = findViewById<TextView>(R.id.txt_rol)

        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid

            // Leer datos desde Firestore
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        txtNombre.text = "Nombre: ${document.getString("nombre") ?: "—"}"
                        txtCorreo.text = "Correo: ${document.getString("email") ?: "—"}"
                        txtGrado.text = "Grado: ${document.getString("grado") ?: "—"}"
                        txtSeccion.text = "Sección: ${document.getString("seccion") ?: "—"}"
                        txtRol.text = "Rol: ${document.getString("role") ?: "—"}"
                    } else {
                        Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }

        } else {
            Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
