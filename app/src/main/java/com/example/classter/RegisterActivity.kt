package com.example.classter

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicialización de Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Referencias a los elementos del layout
        val emailInput = findViewById<EditText>(R.id.register_email)
        val passwordInput = findViewById<EditText>(R.id.register_password)
        val registerButton = findViewById<Button>(R.id.register_button)
        val backButton = findViewById<Button>(R.id.back_to_login_button)
        val spinnerRole = findViewById<Spinner>(R.id.spinner_role)

        // Cargar opciones del Spinner (Apoderado / Estudiante)
        val roles = listOf("Apoderado", "Estudiante")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        // Acción del botón de registro
        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val role = spinnerRole.selectedItem.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // Guardar datos adicionales en Firestore
                        val userData = hashMapOf(
                            "email" to email,
                            "role" to role
                        )

                        db.collection("users").document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registrado como $role", Toast.LENGTH_SHORT).show()

                                // Redirigir según el rol
                                when (role) {
                                    "Apoderado" -> startActivity(Intent(this, ApoderadoActivity::class.java))
                                    "Estudiante" -> startActivity(Intent(this, EstudianteActivity::class.java))
                                    else -> startActivity(Intent(this, HomeActivity::class.java))
                                }
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al guardar en Firestore", Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Botón para regresar al login
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
