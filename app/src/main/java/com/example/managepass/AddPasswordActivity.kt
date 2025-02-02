package com.example.managepass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPasswordActivity : AppCompatActivity() {
    private val TAG = "AddPasswords"
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
        //recureper les données de l'utilisateur nom et prenom
        val user = Firebase.auth.currentUser
        // set welcome_text (textview) to "Welcome $user.displayName"
        findViewById<TextView>(R.id.welcome_text).text = "Bonjour ${user!!.displayName}"
        // Récupérer le bouton  enregistrer pour ajouter des passwords
        val buttonadd = findViewById<Button>(R.id.add_password_button_save);
        // Ajouter un écouteur d'événements sur le bouton
        buttonadd.setOnClickListener {
            // Récupération des valeurs des champs
            val siteurl = findViewById<EditText>(R.id.site_url_input).text.toString()
            val nomutilisateur = findViewById<EditText>(R.id.add_password_placeholder_username).text.toString()
            val email = findViewById<EditText>(R.id.add_password_placeholder_email).text.toString()
            val password = findViewById<EditText>(R.id.register_input_password).text.toString()
            var database = Firebase.firestore
            val tgData = hashMapOf(
                "password" to password,
                "nom utilisateur" to nomutilisateur,
                "email" to email,
                "url" to siteurl,
                "uid" to user.uid
            )
            //recuperer les informationn du listing email password nom d'utilistaeur url du site qui exist sur firabase
            database
                .collection("passwords")
                .add(tgData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(
                        this,
                        "Les informations du site $siteurl ont été ajoutées avec succès.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Redirection vers ListingPasswordActivity
                    val intent = Intent(this, ListingPasswords::class.java)
                    startActivity(intent)
                    finish() // Ferme l'activité actuelle
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }

    }
}