package com.example.managepass

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Edit_Delete_Password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_delete_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Récupérer les données passées via l'Intent
        val nomUtilisateur = intent.getStringExtra("nom_utilisateur")
        val adresseEmail = intent.getStringExtra("adresse_email")
        val urlSite = intent.getStringExtra("url_site")
        val password = intent.getStringExtra("mot_de_passe")
        // Remplir les champs EditText avec les données
        findViewById<EditText>(R.id.edit_username).setText(nomUtilisateur)
        findViewById<EditText>(R.id.edit_adresse_email).setText(adresseEmail)
        findViewById<EditText>(R.id.edit_url_site_input).setText(urlSite)
        findViewById<EditText>(R.id.edit_input_password).setText(password)

    }
}