package com.example.managepass

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class AddPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_password)

        // Gérer les fenêtres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Récupérer l'utilisateur actuel depuis Firebase
        val user = FirebaseAuth.getInstance().currentUser

        // Récupérer les vues
        val welcomeText = findViewById<TextView>(R.id.welcome_text)
        val siteUrlEditText = findViewById<EditText>(R.id.site_url)
        val usernameEditText = findViewById<EditText>(R.id.username)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val userImage = findViewById<ImageView>(R.id.user_image)

        // Vérifier si l'utilisateur est connecté
        if (user != null) {
            // Afficher le message de bienvenue avec le nom de l'utilisateur
            welcomeText.text = "Welcome ${user.displayName ?: "User"}"

            // Remplir les champs avec les données de l'utilisateur
            siteUrlEditText.setText("http://example.com")
            usernameEditText.setText(user.displayName ?: "")
            emailEditText.setText(user.email ?: "")
            passwordEditText.setText("******")
        } else {
            // Si l'utilisateur n'est pas connecté, afficher un message générique
            welcomeText.text = "Welcome Guest"
            siteUrlEditText.setText("")
            usernameEditText.setText("")
            emailEditText.setText("")
            passwordEditText.setText("")
        }

        // Ajouter des écouteurs d'événements pour le bouton Enregistrer
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            // Récupérer les données saisies dans les champs
            val siteUrl = siteUrlEditText.text.toString()
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Sauvegarder les données ou effectuer une autre action (par exemple, dans une base de données)
        }
    }
}