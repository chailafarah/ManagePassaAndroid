package com.example.managepass

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
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Récupérer le bouton continue par son ID
        val button = findViewById<Button>(R.id.ih);
        // Ajouter un écouteur d'événements sur le bouton
        button.setOnClickListener {
            // Ouvrir l'activité SignInActivity
            startActivity(Intent(this, SignInActivity::class.java))
        }
        // Récupérer le bouton signup par son ID
        val button2 = findViewById<Button>(R.id.signup);
        // Ajouter un écouteur d'événements sur le bouton
        button2.setOnClickListener {
            // Ouvrir l'activité SignInActivity
            startActivity(Intent(this, signup::class.java))
        }

    }
}