package com.example.classter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ApoderadoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apoderado)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val txtNombreApoderado = findViewById<TextView>(R.id.txt_nombre_apoderado)
        val txtCorreoApoderado = findViewById<TextView>(R.id.txt_correo_apoderado)
        val btnVerHijos = findViewById<Button>(R.id.btn_ver_hijos)
        val btnCerrarSesion = findViewById<Button>(R.id.btn_cerrar_sesion)

        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        txtNombreApoderado.text = "Apoderado: ${document.getString("nombre")}"
                        txtCorreoApoderado.text = "Correo: ${document.getString("email")}"
                    }
                }
        }

        btnVerHijos.setOnClickListener {
            startActivity(Intent(this, HijosActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
